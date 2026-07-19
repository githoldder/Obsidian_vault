# AI-Native Project Delivery Model

## Double Pyramid

```text
Human Decision Pyramid
Vision -> Roadmap -> Epic -> Sprint -> User Story -> Feature -> Acceptance Criteria -> PRD.md

Compile
PRD.md -> Task Graph / Work Unit JSON

AI Execution Inverted Pyramid
Planner -> Research -> Builder -> Reviewer -> QA -> DevOps -> Delivery -> Feedback
```

## Artifact Split

| Plane | Artifact | Owner |
|---|---|---|
| Decision | Vision, Epic, Sprint, User Story, Feature, Acceptance Criteria | Human |
| Knowledge | PRD.md, SRS, HLD, LLD, DB design, API docs | Human + Agent |
| Protocol | Task.json, Sprint.json, Queue.json, State.json | Compiler + Agent |
| Execution | code, tests, deploy config, release notes | Agent |
| Feedback | QA report, risk report, ADR, retrospective | QA + Human |

## Work Unit JSON Shape

```json
{
  "id": "WU-001",
  "source": {
    "prd": "docs/prd/001-prd.md",
    "epic": "E-001",
    "sprint": "S-001",
    "story": "US-001",
    "feature": "F-001"
  },
  "goal": "",
  "owner": "planner|research|frontend|backend|reviewer|qa|devops",
  "status": "draft|ready|assigned|running|review|testing|blocked|done|archived",
  "priority": "P0|P1|P2",
  "allowed_scope": {
    "files": [],
    "actions": []
  },
  "constraints": [],
  "outputs": [],
  "verification": [],
  "history": []
}
```

## Quality Gates

- Scope gate: work traces back to PRD.
- Design gate: architecture and interfaces are coherent.
- Review gate: code/docs stay within allowed scope.
- QA gate: acceptance and regression risks are tested.
- Release gate: deploy, rollback, monitoring, and release note are ready.
