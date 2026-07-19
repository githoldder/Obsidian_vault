---
name: governance-core-pack
description: Governance and entropy-reduction skillpack for skill library cleanup, Darwin-style use-it-or-retire-it evaluation, engineering safety, think-before-execute checks, naming/version rules, Git checkpoint planning, quality review, migration planning, and Codex/Anthropic skill structure adaptation. Use when organizing skills, prompts, SOPs, templates, repos, or knowledge bases.
---

# Governance Core Pack

Use this skill to keep the skill library small, survivable, and easy for agents to use.

## Workflow

1. Scan structure, file types, `SKILL.md` frontmatter, duplicated content, and risky files.
2. Classify each asset:
   - runnable skill;
   - reference;
   - template/asset;
   - script;
   - archive;
   - delete candidate.
3. Prefer flat Codex skill structure: `skills/<skill-name>/SKILL.md`.
4. Keep `SKILL.md` lightweight; move long SOPs to `references/`.
5. Preserve original files before migration.
6. Apply Darwin scoring: keep, merge, archive, script, or delete.
7. Produce an audit report, target tree, migration list, and validation result.

## Reference Routing

- Read `references/skill-structure.md` for Codex/Anthropic-compatible structure.
- Read `references/source-map.md` when original governance notes or legacy sources are needed.

## Safety Rules

- Do not delete source material until a backup or archive exists.
- Do not rewrite Obsidian links unless migration is complete.
- For Git repositories, inspect status before moving repository internals.
- For large changes, create a staged copy first, validate it, then sync.

## Output Contract

Return:

- entropy score;
- findings by severity;
- target structure;
- migration plan;
- files changed;
- validation commands/results.
