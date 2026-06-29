# Traceability Skill

## Purpose

Maintain trace links from design and requirements through API contracts to Java implementation.

## Trace Chain

```
Requirement ID → Figma frame → OpenAPI operationId → Controller → Service → Business rule
```

## Required Links

| Artifact | Must link to |
|----------|--------------|
| OpenAPI operation | Requirement ID in `description` or `x-requirement-id` |
| Service method | JavaDoc or comment with requirement ID |
| Validator | Business rule ID |
| Event | Workflow step ID |
| Test | Requirement acceptance criteria |

## Sub-Skills

| Topic | File |
|-------|------|
| Figma → backend | [figma-to-backend-traceability.md](figma-to-backend-traceability.md) |
| Layer placement | [platform-product-solution-traceability.md](platform-product-solution-traceability.md) |
| API trace | [api-traceability.md](api-traceability.md) |
| Business rules | [business-rule-traceability.md](business-rule-traceability.md) |
| Observability | [observability-traceability.md](observability-traceability.md) |

## Output Template

| Req ID | Figma | API operationId | Class.Method | Rule ID | Test |
|--------|-------|-----------------|--------------|---------|------|

Run [../checklists/final-output-checklist.md](../checklists/final-output-checklist.md) before delivery.
