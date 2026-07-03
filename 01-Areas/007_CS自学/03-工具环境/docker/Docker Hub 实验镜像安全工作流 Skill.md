---
name: docker-hub-lab-safety
description: Use when managing Docker images for reusable coursework or lab environments, especially Docker Hub push/pull, docker commit before cleanup, verifying remote images before deletion, avoiding irreversible image loss, and preparing experiment report handoff artifacts such as final txt sections and screenshot scripts.
metadata:
  short-description: Safe Docker Hub lab image workflow
---

# Docker Hub Lab Safety

Use this skill for course/lab Docker images that must be reused later. The core rule is: **never treat a local image as safely disposable until a remote pull or manifest check has proved the pushed image exists**.

## Safety Rules

- Before deleting a container with meaningful experiment state, run `docker commit` and record the new image ID.
- Before deleting any local image, prove one of these is true:
  - `docker pull <repo>:<tag>` succeeds and returns the expected digest.
  - `docker buildx imagetools inspect <repo>:<tag>` shows the expected remote digest/platform.
- Do not rely on memory, conversation claims, or tag names alone as proof of upload.
- Do not run `docker image prune`, `docker system prune`, `docker rmi`, or bulk volume cleanup when the user is worried about recoverability unless the user explicitly approves that exact cleanup after verification.
- Prefer deleting stopped/test containers before deleting images. Keep important images until remote verification is complete.
- Record commands and evidence in a project doc or process log before cleanup.

## Push Workflow

1. Identify the active container and image:

```bash
docker ps -a
docker image ls
docker inspect <container-or-image>
```

2. If the container contains recent experiment state, commit it:

```bash
docker commit <container> <dockerhub-user>/<repo>:<tag>
docker image inspect <dockerhub-user>/<repo>:<tag>
```

3. Push to Docker Hub:

```bash
docker push <dockerhub-user>/<repo>:<tag>
```

4. Treat push as complete only when Docker prints a final digest line:

```text
<tag>: digest: sha256:... size: ...
```

5. Verify the remote independently:

```bash
docker buildx imagetools inspect <dockerhub-user>/<repo>:<tag>
docker pull <dockerhub-user>/<repo>:<tag>
```

The digest from `push`, `imagetools inspect`, and `pull` should match.

## Pull/Run Reuse Workflow

Keep a small script for repeated lab use:

```bash
docker pull <dockerhub-user>/<repo>:<tag>
docker run -itd --name <lab-container> <dockerhub-user>/<repo>:<tag>
docker exec -it <lab-container> bash
```

For project scripts, include commands for:

- `pull`
- `run`
- `shell`
- `commit`
- `push`
- `verify-remote`
- `stop`
- `clean` for container-only cleanup

`clean` should remove containers only. Image deletion should be a separate explicit action.

## Reproducible Rebuild Fallback

If a local image was deleted before upload verification, do not overpromise recovery. Check:

- `docker events`
- `docker history`
- `docker image ls -a`
- remaining Dockerfiles/build contexts
- copied source code and datasets
- course resource directories

If exact recovery is impossible, switch to a reproducible rebuild plan:

- rebuild from Dockerfile and local resources
- use domestic mirrors when possible, such as Tsinghua TUNA for Apache downloads
- avoid unnecessary network downloads by reusing local tarballs and jars
- run functional tests inside a container
- push and verify the rebuilt image before any cleanup

## Lab Report Handoff

For coursework experiments, the final handoff may not be a polished Word file. Ask what artifact the user actually needs. When the user expects manual copy/paste and screenshots:

- Use the template from `01-resources/<exam>/<exam>.md`.
- Usually only fill the missing sections, such as `五、实验过程与结果` and `六、实验结果分析与体会`.
- Put the final copy-ready text in `03-reports`.
- Put screenshot instructions in `02-process/script`.
- The screenshot script should say:
  - what command to type
  - what result should be visible
  - when to screenshot
  - suggested screenshot filename
  - where to insert the screenshot in the document

Do not rewrite sections that are already completed in the template unless the user asks.

## Evidence Checklist

Before saying the task is done, report:

- Docker Hub repository and tag.
- Remote digest.
- Whether `docker pull` succeeded.
- What local containers/images remain.
- What, if anything, was deleted.
- Where the reusable scripts and final report artifacts are saved.
