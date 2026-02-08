#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"

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

export IMAGE_NAME
export IMAGE_TAG
"${SCRIPT_DIR}/build-image.sh" "${VSIX_PATH}"

docker push "${IMAGE_NAME}:${IMAGE_TAG}"

if [[ "${PUSH_LATEST:-0}" == "1" ]]; then
  docker tag "${IMAGE_NAME}:${IMAGE_TAG}" "${IMAGE_NAME}:latest"
  docker push "${IMAGE_NAME}:latest"
fi

echo "Pushed image: ${IMAGE_NAME}:${IMAGE_TAG}"
