# Aurora VSIX Deployment Guide

This document explains what we did on February 8, 2026 and how to repeat it in future.

Reference log:
- deploy/logs/logs_depl_8_Feb_2026.txt

## Goal

Deploy a new VSIX-based OpenVSCode image to VM with near zero downtime.

How this works:
- Keep the old container running.
- Start the new container on a different localhost port.
- Health check the new container.
- Switch NGINX upstream to the new port.
- Remove the old container after successful switch.

## Files used in this repo

- deploy/Dockerfile
- deploy/build-image.sh
- deploy/build-and-push.sh
- deploy/deploy-vm.sh
- deploy/vm/deploy-bluegreen.sh

## Part 1: Build and push image from VSIX on local machine

1. Go to the auroraSjsExt repository folder.
2. Log in to Docker.
3. Set image name to sudojarvis/aurora-openvscode.
4. Set a unique image tag using UTC timestamp.
5. Build and push using deploy/build-and-push.sh and pass the VSIX file path.
6. Optionally update latest along with the immutable tag.

Example successful tag from Feb 8, 2026:
- sudojarvis/aurora-openvscode:20260208-165319

## Part 2: One-time NGINX setup on VM for blue/green switching

Before this change, NGINX was proxying directly to localhost port 3000.

We changed NGINX to this model:
- One upstream definition named aurora_openvscode.
- One include file that points to the currently active local port.

Setup summary:
1. Create aurora-upstream.conf in NGINX conf.d.
2. Create aurora-upstream.inc with initial target 127.0.0.1:3000.
3. Update NGINX site files to use aurora_openvscode upstream instead of direct 127.0.0.1:3000 proxy.
4. Validate NGINX config.
5. Reload NGINX.
6. Verify endpoint health through the public domain.

Expected health response may be 401 because basic auth is enabled.

## Part 3: What was done on Feb 8, 2026 (actual rollout)

Initial state:
- Old container: aurora-ide
- Old route: localhost port 3000

Rollout actions:
1. Pulled new image tag 20260208-165319.
2. Started new container aurora-openvscode-blue on localhost port 18080.
3. Verified local health on port 18080 and got HTTP 200.
4. Updated NGINX upstream include to point to 18080.
5. Validated and reloaded NGINX.
6. Verified public endpoint still healthy.
7. Removed old container aurora-ide.

Result:
- Active container is aurora-openvscode-blue on 127.0.0.1:18080.
- Traffic is routed through NGINX upstream include file.

## Part 4: Standard process for future releases

For every release:
1. Build and push new immutable image tag from local machine.
2. On VM, start container on inactive color port.
3. Health check the new port locally.
4. Switch NGINX upstream include to the new port.
5. Validate and reload NGINX.
6. Remove old color container.

## Blue/Green mapping

- Blue container name: aurora-openvscode-blue
- Blue host port: 18080
- Green container name: aurora-openvscode-green
- Green host port: 18081

Alternate between blue and green each deployment.

If blue is active now, next release should start green. After switching to green, remove blue.
On the next release after that, start blue again and switch back.

## Part 5: GitHub Actions CI/CD with deploy branch

We also added GitHub Actions workflows so releases can run through CI/CD.

Branch model:
- `main` for normal development.
- `deploy` for release-ready deployment commits.

Workflows added:
- `.github/workflows/build-and-push.yml`
- `.github/workflows/deploy-vm.yml`
- `.github/workflows/rollback-vm.yml`

How they work:
- `build-and-push.yml` runs on push to `deploy` and builds then pushes the Docker image.
- The image tag format is based on commit SHA: `sha-<12-char-commit>`.
- `deploy-vm.yml` can run automatically after successful build on `deploy`, or be run manually.
- `rollback-vm.yml` is manual and deploys any previous image ref you provide.

Recommended GitHub repository secrets:
- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`
- `VM_HOST`
- `VM_USER`
- `VM_PORT` (optional)
- `VM_SSH_KEY`

Recommended GitHub repository variables:
- `DOCKER_IMAGE_NAME` with value `sudojarvis/aurora-openvscode`
- `REMOTE_DEPLOY_DIR` with value `/opt/aurora-deploy`

Recommended branch and environment controls:
- Protect `deploy` branch with pull request requirement.
- Use GitHub Environment `production` with required reviewer approval for deployment jobs.

## Rollback approach

Rollback means:
1. Start previous known good image on inactive color port.
2. Health check it.
3. Switch NGINX upstream include back to that port.
4. Reload NGINX.
5. Remove the failed deployment container.

## Quick operational checks

Use these checks after every deployment:
- Verify running containers and image tags.
- Verify current upstream include target in NGINX.
- Verify NGINX configuration test passes.
- Verify public endpoint responds as expected.

## Notes

- Layer already exists during docker push is normal.
- Always deploy using immutable tags for traceability.
- Keep latest only as a convenience pointer, not as release history.
