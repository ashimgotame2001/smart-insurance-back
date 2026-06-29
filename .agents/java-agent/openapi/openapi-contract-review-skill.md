# OpenAPI Contract Review Skill

## Purpose

Review OpenAPI specs before implementation or merge.

## Review Dimensions

1. **Paths** — follow [api-path-standard-skill.md](api-path-standard-skill.md)
2. **Schemas** — naming, required fields, money/date formats
3. **Status codes** — complete success and error coverage
4. **Pagination** — list operations include page params and wrapper
5. **Security** — operations declare `security` requirements
6. **Consistency** — aligns with existing module APIs
7. **Traceability** — operationId maps to requirement ID

## Output

| Operation | Finding | Severity | Recommendation |
|-----------|---------|----------|----------------|

Run [../checklists/api-review-checklist.md](../checklists/api-review-checklist.md) for full checklist.
