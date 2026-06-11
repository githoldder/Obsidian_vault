---
name: think-before-execute
description: >
  Engineering safeguard skill that enforces default engineering policy (minimal change,
  backward compatibility, no unnecessary refactoring), dependency audit, breaking changes
  hard rules, context sufficiency check, architecture drift control, and security red lines.

  Trigger for tasks involving: modifying files, writing code, architectural decisions,
  fixing bugs, implementing features, refactoring, or any non-trivial response.

  Coordinates with brainstorming: if brainstorming is also triggered, this skill
  supplements after brainstorming completes — only checking engineering policy
  compliance, breaking changes, security, and architecture consistency.

  Skip for: formatting, trivial typo fixes, single-file changes with clear instructions,
  simple text transforms, minor style tweaks, or read-only queries.

  When in doubt, trigger — it's better to pause briefly than barrel down the wrong path.
---

# Think-Before-Execute

Engineering safeguard: catch what brainstorming doesn't cover.

---

## Default Engineering Policy

When user does not specify, these policies are **ALL enabled by default** (no need to confirm each):

| # | Policy | Description |
|---|--------|-------------|
| EP-01 | Minimal Change | Only modify what must be modified — nothing extra |
| EP-02 | Backward Compatibility | Do not break existing APIs, data structures, or env var conventions |
| EP-03 | No Unnecessary Refactoring | Do not touch code unrelated to the task |
| EP-04 | No New Dependencies | Use stdlib first; only add third-party when task explicitly requires |
| EP-05 | No Public API Changes | Do not modify Public APIs unless user explicitly asks |
| EP-06 | Reuse Existing Patterns | Follow project's existing style, architecture, and conventions |
| EP-07 | Local-First Fixes | Prefer localized replacement over diffuse refactoring; minimize diff |
| EP-08 | Architecture Drift Control | Align with project's existing structure; do not re-architect |
| EP-09 | Security Red Line | Never hardcode API keys, passwords, or sensitive env vars in source |

### Anti Over-Engineering

Do **NOT** pre-abstract, over-generalize, introduce complex layers, create extra framework/manager/service, or do large directory restructuring when the task is:

- Bug fix
- Small feature
- Minor refactoring
- Doc change
- Simple utility function

Unless user explicitly requests it. "Good enough and maintainable" > "Theoretically optimal architecture".

---

## Coordination with Brainstorming

| Scenario | Behavior |
|----------|----------|
| Both brainstorming AND this skill triggered | Brainstorming runs **first** (intent exploration + design). This skill supplements **after** — only checks EP compliance, BC, security, architecture |
| Brainstorming NOT triggered (e.g., bug fix, CRUD, doc edit) | This skill executes **all phases** |

---

## Task Classification — Routing

```
Receive task
  │
  ├─ Privilege指令 (-y/--force/direct) → Execute directly, skip phases
  │
  ├─ Fast-Execution path (small edit / Bug fix / CRUD / style / doc)
  │   → Pre-execution restate → Execute
  │
  └─ Non-simple task → Phase 1
```

> **Lightweight principle**: Audit completes in one reasoning cycle. Process is for high-risk tasks, not all tasks.

---

## Phase 1: Internal Audit

All checks are **internal reasoning** — not output to user.

### 1.1 Goal Extraction
What does the user actually want? Read beyond the literal, but avoid over-interpretation.

### 1.2 Context Sufficiency Check
Flag missing items that would significantly affect decisions:

- Error messages / Stack Trace
- Relevant code
- File structure
- Runtime environment
- Reproduction steps
- Config files
- I/O examples

**Minimum context principle**: Only request info that genuinely affects decisions. Do not ask for the entire project at once. Do not speculate implementation details.

### 1.3 Dependency Audit
Identify blast radius: Will this change break existing APIs, env vars, or cross-file dependencies? Cause cascading changes?

> **Hard Rule**: Breaking Changes detected (modifying public API signatures, deleting/renaming exports, changing data structures, changing env var conventions) → **Must trigger structured inquiry**.

### 1.4 Feasibility Check
Is the task technically infeasible? Prohibitively expensive? Fundamentally conflicts with existing architecture?

> If infeasible or cost >> benefit: do not force. In inquiry, explain the problem, quantify impact, offer tradeoffs.

### 1.5 Risk & Path Divergence Identification
Are there semantic ambiguities, or multiple distinct technical paths? How large is the cost difference between paths?

### 1.6 Rework Cost Assessment
If the wrong direction is chosen, what's the rework cost? 5 minutes or 2 hours?

---

## Phase 2: Decision

**No more numeric confidence scores.** Use conditional logic:

### Condition A: Direct Execution

Execute directly if **ALL** of the following are met:
- Goal is clear
- Implementation path is essentially unique
- Low risk
- Low rework cost
- No Breaking Changes
- No missing critical context

→ Pre-execution restate → Execute

### Condition B: Trigger Structured Inquiry

Trigger Phase 3 if **ANY** of the following exists:
- Goal is unclear
- Multiple reasonable paths with significant cost difference
- Breaking Changes involved
- Missing critical context
- High risk or high rework cost

→ Pause execution → Phase 3

---

## Phase 3: Structured Inquiry

### Pre-warning: Architecture/Feasibility Conflict

If Phase 1 found the task infeasible, expensive, or arch-incompatible:

```
🛑 Architecture / Risk Warning:

[Why infeasible / expensive / conflicts with architecture]
[Quantified impact and cost]
[Available tradeoffs if any]

💡 To proceed, choose a direction:
```

### Standard Inquiry Format

```
⚠️ 需要确认 / Confirmation Needed:

[Brief description of ambiguity or missing critical condition]

💡 Please choose (reply with number):
- [1] [Option A + cost/scenario]
- [2] [Option B + cost/scenario]
- [3] Other
- [4] ✏️ Write a Demo — <10 line example for preview, then decide
```

### When to Use What

| Format | Applicable Scenarios |
|--------|----------------------|
| Multiple-choice (options above) | Path known, decision points clear, user chooses from finite options |
| Open diagnostic | Root cause unknown, behavior unexpected — do NOT force multiple-choice |

### Inquiry Design Principles

- **Ask with solutions** — Never ask open-ended "what do you mean?" Instead: "I see two reasonable paths: A... B... Which do you prefer?"
- **Max 2-4 questions per turn** — More than 4 means the task hasn't converged; internally consolidate first
- **Prioritize decision-critical questions** — Don't throw every uncertainty at the user
- **Always include Demo option** — When direction is uncertain, user can preview a sample first
- **Mark recommended bias** — When a clearly better option exists

---

## Pre-Execution Restate (Execution Alignment)

Before executing (whether after direct execution or inquiry response):

```
I understand the goal as:
- [Core objective]
- [Scope of change]
- [Key constraints / Default engineering policies applied]

If correct, I will begin execution.
```

Goal: reduce silently misunderstanding.

---

## Explicit Assumptions

If making decisions based on default inference (e.g., "I default to SSR deployment, so no client-side caching"), state key assumptions before execution. Prevents "AI quietly assumes requirements."

---

## Light Decision Log (Optional)

If the inquiry involved significant decisions (scheme selection, architecture tradeoffs, Breaking Changes), after execution briefly record 2-3 sentences:

- What was chosen
- Why this choice
- What was abandoned

Place in file header comment or CHANGELOG — not a separate doc.

---

## Default Timeout Strategy

> **Interactive mode**: After sending inquiry, stay suspended. Do NOT autonomously push code changes. In local dev, "silent unconfirmed modifications" are harder to handle than "stalling." User may be busy; returning to an unconfirmed Diff is more disruptive than waiting.

> **CI/CD / background batch**: Only apply when explicitly in non-interactive mode — default to minimal change + backward-compatible direction; do not modify Public APIs, do not add dependencies, do not do destructive refactoring.

---

## Full Flow Diagram

```
Receive task
  │
  ├─ Privilege指令 (-y/--force) → Execute directly
  │
  ├─ Fast-Execution (Bug fix / CRUD / style / doc)
  │   → Pre-execution restate → Execute
  │
  └─ Non-simple task
        │
        ▼
  Phase 1: Internal Audit
        │ Goal → Context check → Dependency → Feasibility → Risk → Rework cost
        │ (Breaking Changes flagged)
        │
        ▼
  Phase 2: Decision
        │
        ├── Condition A met → Pre-execution restate → Execute
        │
        └── Condition B triggered → Phase 3: Structured Inquiry
                                      │ 🛑 Arch conflict? → Pre-warning first
                                      │ ⚠️ Multiple-choice (2-4 q, Demo option)
                                      │ → Got reply → Pre-execution restate → Execute
                                      │ → Light decision log (optional)
                                      │ → Interactive: stay suspended
                                      │ → CI/CD: default timeout strategy
```