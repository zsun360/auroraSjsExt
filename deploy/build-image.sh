#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
DOCKERFILE="${SCRIPT_DIR}/Dockerfile"

IMAGE_NAME="${IMAGE_NAME:-}"
IMAGE_TAG="${IMAGE_TAG:-$(date +%Y%m%d-%H%M%S)}"
VSIX_PATH="${1:-}"

if [[ -z "${IMAGE_NAME}" ]]; then
  echo "IMAGE_NAME is required. Example: IMAGE_NAME=sudojarvis/aurora-openvscode $0"
  exit 1
fi

if [[ -z "${VSIX_PATH}" ]]; then
  VSIX_PATH="$(ls -t "${ROOT_DIR}"/*.vsix 2>/dev/null | head -n1 || true)"
fi

if [[ -z "${VSIX_PATH}" || ! -f "${VSIX_PATH}" ]]; then
  echo "No VSIX found. Pass path explicitly: $0 /path/to/file.vsix"
  exit 1
fi

VSIX_BASENAME="$(basename "${VSIX_PATH}")"

if command -v realpath >/dev/null 2>&1; then
  VSIX_PATH="$(realpath "${VSIX_PATH}")"
else
  VSIX_PATH="$(cd "$(dirname "${VSIX_PATH}")" && pwd)/$(basename "${VSIX_PATH}")"
fi

if [[ "${VSIX_PATH}" != "${ROOT_DIR}/"* ]]; then
  echo "VSIX must be inside build context: ${ROOT_DIR}"
  echo "Provided: ${VSIX_PATH}"
  exit 1
fi

echo "Building ${IMAGE_NAME}:${IMAGE_TAG}"
echo "Using VSIX: ${VSIX_BASENAME}"

docker build \
  -f "${DOCKERFILE}" \
  --build-arg VSIX_FILE="${VSIX_BASENAME}" \
  --build-arg WORKSPACE_SEED_DIR="auroraFiles" \
  -t "${IMAGE_NAME}:${IMAGE_TAG}" \
  "${ROOT_DIR}"

if [[ "${TAG_LATEST:-0}" == "1" ]]; then
  docker tag "${IMAGE_NAME}:${IMAGE_TAG}" "${IMAGE_NAME}:latest"
fi

echo "Built image: ${IMAGE_NAME}:${IMAGE_TAG}"
