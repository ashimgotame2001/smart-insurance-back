# Review Skill

## Purpose

Master index for reviewing Smart Remittance Java code, APIs, and architecture.

## Review Workflow

1. Identify review scope (module, PR, feature)
2. Load relevant sub-review files below
3. Run matching checklist from `checklists/`
4. Report findings with severity and evidence

## Severity

| Level | Meaning |
|-------|---------|
| **Critical** | Must fix — security, data loss, contract break |
| **Major** | Should fix — architecture violation, missing validation |
| **Minor** | Nice to fix — naming, minor consistency |

## Sub-Reviews

| Dimension | File |
|-----------|------|
| Platform compliance | [platform-compliance-review.md](platform-compliance-review.md) |
| Product compliance | [product-compliance-review.md](product-compliance-review.md) |
| Solution compliance | [solution-compliance-review.md](solution-compliance-review.md) |
| Architecture | [architecture-review.md](architecture-review.md) |
| Runtime switching | [runtime-switching-review.md](runtime-switching-review.md) |
| JPA | [jpa-review.md](jpa-review.md) |
| API design | [api-design-review.md](api-design-review.md) |
| Performance | [performance-review.md](performance-review.md) |
| Security | [security-review.md](security-review.md) |
| Observability | [observability-review.md](observability-review.md) |
| Logging | [logging-review.md](logging-review.md) |

## Output Format

```markdown
# Review: [scope]

## Summary
[1-2 sentences]

## Findings
### Critical
- [file:line] description → recommendation

### Major
...

### Minor
...
```

Use prompt [../prompts/review-code.md](../prompts/review-code.md) for structured reviews.
