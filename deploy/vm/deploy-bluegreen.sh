#!/usr/bin/env bash
set -euo pipefail

IMAGE_REF="${IMAGE_REF:-}"
APP_NAME="${APP_NAME:-aurora-openvscode}"
NETWORK_NAME="${NETWORK_NAME:-aurora-net}"
CONTAINER_PORT="${CONTAINER_PORT:-3000}"
BLUE_PORT="${BLUE_PORT:-18080}"
GREEN_PORT="${GREEN_PORT:-18081}"
NGINX_UPSTREAM_FILE="${NGINX_UPSTREAM_FILE:-/etc/nginx/conf.d/aurora-upstream.inc}"
HEALTHCHECK_PATH="${HEALTHCHECK_PATH:-/}"
HEALTHCHECK_TIMEOUT_SECS="${HEALTHCHECK_TIMEOUT_SECS:-120}"

if [[ -z "${IMAGE_REF}" ]]; then
  echo "IMAGE_REF is required. Example: IMAGE_REF=sudojarvis/aurora-openvscode:20260208-120000 $0"
  exit 1
fi

if [[ "${EUID}" -ne 0 ]]; then
  SUDO="sudo"
else
  SUDO=""
fi

container_exists() {
  docker ps -a --format '{{.Names}}' | grep -Fxq "$1"
}

container_running() {
  docker ps --format '{{.Names}}' | grep -Fxq "$1"
}

active_color=""
if container_running "${APP_NAME}-blue"; then
  active_color="blue"
elif container_running "${APP_NAME}-green"; then
  active_color="green"
fi

if [[ "${active_color}" == "blue" ]]; then
  next_color="green"
  next_port="${GREEN_PORT}"
  old_container="${APP_NAME}-blue"
  new_container="${APP_NAME}-green"
else
  next_color="blue"
  next_port="${BLUE_PORT}"
  old_container="${APP_NAME}-green"
  new_container="${APP_NAME}-blue"
fi

echo "Active color: ${active_color:-none}"
echo "Deploying color: ${next_color}"
echo "Image: ${IMAGE_REF}"

docker network inspect "${NETWORK_NAME}" >/dev/null 2>&1 || docker network create "${NETWORK_NAME}"

docker pull "${IMAGE_REF}"

if container_exists "${new_container}"; then
  docker rm -f "${new_container}" >/dev/null
fi

docker run -d \
  --name "${new_container}" \
  --restart unless-stopped \
  --network "${NETWORK_NAME}" \
  -p "127.0.0.1:${next_port}:${CONTAINER_PORT}" \
  "${IMAGE_REF}" >/dev/null

echo "Waiting for health on 127.0.0.1:${next_port}${HEALTHCHECK_PATH}"
started_at="$(date +%s)"
while true; do
  code="$(curl -k -s -o /dev/null -w '%{http_code}' "http://127.0.0.1:${next_port}${HEALTHCHECK_PATH}" || true)"
  if [[ "${code}" == "200" || "${code}" == "302" || "${code}" == "401" ]]; then
    break
  fi

  now="$(date +%s)"
  elapsed=$((now - started_at))
  if (( elapsed > HEALTHCHECK_TIMEOUT_SECS )); then
    echo "Health check failed for ${new_container}; removing new container"
    docker logs --tail 120 "${new_container}" || true
    docker rm -f "${new_container}" >/dev/null || true
    exit 1
  fi
  sleep 2
done

old_upstream_content=""
if [[ -f "${NGINX_UPSTREAM_FILE}" ]]; then
  old_upstream_content="$(cat "${NGINX_UPSTREAM_FILE}")"
fi

echo "server 127.0.0.1:${next_port};" | ${SUDO} tee "${NGINX_UPSTREAM_FILE}" >/dev/null

if ! ${SUDO} nginx -t; then
  echo "Nginx config test failed; restoring upstream config"
  if [[ -n "${old_upstream_content}" ]]; then
    printf '%s\n' "${old_upstream_content}" | ${SUDO} tee "${NGINX_UPSTREAM_FILE}" >/dev/null
  fi
  ${SUDO} nginx -t || true
  docker rm -f "${new_container}" >/dev/null || true
  exit 1
fi

if command -v systemctl >/dev/null 2>&1; then
  ${SUDO} systemctl reload nginx
else
  ${SUDO} nginx -s reload
fi

if [[ -n "${active_color}" && container_exists "${old_container}" ]]; then
  docker rm -f "${old_container}" >/dev/null || true
fi

echo "Deployment complete. Active container: ${new_container}"
echo "Nginx upstream now points to 127.0.0.1:${next_port}"
