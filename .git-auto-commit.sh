#!/bin/bash
# Auto-commit wrapper with lock cleanup (断点续传)
set -e

VAULT_DIR="$HOME/projects/Obsidian_root"
cd "$VAULT_DIR"

# Clean stale lock files (from crashed git processes)
rm -f .git/HEAD.lock .git/index.lock .git/refs/heads/master.lock 2>/dev/null

# Only commit if there are changes
if [[ -z $(git status --porcelain) ]]; then
  exit 0
fi

git add -A
TIMESTAMP=$(date '+%Y-%m-%d %H:%M')
git commit -m "auto: vault sync $TIMESTAMP" || {
  # If commit fails, cleanup locks and retry once
  rm -f .git/HEAD.lock .git/index.lock .git/refs/heads/master.lock 2>/dev/null
  sleep 2
  git add -A
  git commit -m "auto: vault sync $TIMESTAMP (retry)"
}
