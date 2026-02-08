#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
VM_HOST="${VM_HOST:-}"
VM_USER="${VM_USER:-ubuntu}"
VM_PORT="${VM_PORT:-22}"
SSH_KEY_PATH="${SSH_KEY_PATH:-}"
REMOTE_DEPLOY_DIR="${REMOTE_DEPLOY_DIR:-/opt/aurora-deploy}"
IMAGE_REF="${IMAGE_REF:-}"

if [[ -z "${VM_HOST}" || -z "${IMAGE_REF}" ]]; then
  echo "Required env vars: VM_HOST and IMAGE_REF"
  echo "Example: VM_HOST=35.x.x.x IMAGE_REF=sudojarvis/aurora-openvscode:20260208-120000 $0"
  exit 1
fi

SSH_OPTS=("-p" "${VM_PORT}" "-o" "StrictHostKeyChecking=accept-new")
if [[ -n "${SSH_KEY_PATH}" ]]; then
  SSH_OPTS+=("-i" "${SSH_KEY_PATH}")
fi

REMOTE="${VM_USER}@${VM_HOST}"

ssh "${SSH_OPTS[@]}" "${REMOTE}" "mkdir -p '${REMOTE_DEPLOY_DIR}'"
scp "${SSH_OPTS[@]}" "${SCRIPT_DIR}/vm/deploy-bluegreen.sh" "${REMOTE}:${REMOTE_DEPLOY_DIR}/deploy-bluegreen.sh"

ssh "${SSH_OPTS[@]}" "${REMOTE}" "chmod +x '${REMOTE_DEPLOY_DIR}/deploy-bluegreen.sh' && IMAGE_REF='${IMAGE_REF}' '${REMOTE_DEPLOY_DIR}/deploy-bluegreen.sh'"
