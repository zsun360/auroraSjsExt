FROM gitpod/openvscode-server:latest

ENV OPENVSCODE_SERVER_ROOT=/home/.openvscode-server
ENV OPENVSCODE="${OPENVSCODE_SERVER_ROOT}/bin/openvscode-server"

USER root
RUN apt-get update \
    && apt-get install -y --no-install-recommends openjdk-21-jre-headless \
    && rm -rf /var/lib/apt/lists/*

USER openvscode-server

ARG VSIX_FILE=aurora-sjs-ext-0.0.1.vsix
ARG WORKSPACE_SEED_DIR=auroraFiles

COPY --chown=openvscode-server:openvscode-server ${VSIX_FILE} /tmp/aurora-extension.vsix
COPY --chown=openvscode-server:openvscode-server ${WORKSPACE_SEED_DIR}/ /home/workspace/auroraFiles/

RUN mkdir -p /home/workspace/recordings \
    && ${OPENVSCODE} --install-extension /tmp/aurora-extension.vsix \
    && rm -f /tmp/aurora-extension.vsix
