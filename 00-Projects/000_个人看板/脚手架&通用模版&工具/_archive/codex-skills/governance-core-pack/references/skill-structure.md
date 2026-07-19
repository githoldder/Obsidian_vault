# Codex / Anthropic Skill Structure

Use this structure for installable skills:

```text
skills/
├── skill-name/
│   ├── SKILL.md
│   ├── references/
│   ├── scripts/
│   └── assets/
└── another-skill/
    └── SKILL.md
```

Rules:

- The folder name should match `name` in `SKILL.md`.
- Use lowercase letters, digits, and hyphens.
- Put triggering information in frontmatter `description`.
- Keep `SKILL.md` short and procedural.
- Put long domain knowledge in `references/`.
- Put deterministic repeatable operations in `scripts/`.
- Put reusable templates, example projects, and media in `assets/`.
- Avoid README, changelog, and unrelated docs inside installable skill folders.

For Obsidian, keep human-facing README files outside the installable skill root, or treat them as project documentation rather than skill runtime content.
