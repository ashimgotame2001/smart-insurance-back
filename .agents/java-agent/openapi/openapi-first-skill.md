# OpenAPI First Skill

## Purpose

Define and implement APIs contract-first using OpenAPI 3.x before writing controller code.

## Workflow

1. Analyze functional requirement and Figma fields
2. Draft or update OpenAPI YAML in module `src/main/resources/openapi/`
3. Review contract ([openapi-contract-review-skill.md](openapi-contract-review-skill.md))
4. Generate or hand-write controller implementing contract
5. Ensure integration tests validate against spec

## Principles

- OpenAPI is source of truth for path, method, schema, status codes
- Controller method signatures must match generated interface
- Breaking changes require version bump or new path prefix
- Document all error responses with standard error schema

## File Location

```
sr-<module>/src/main/resources/openapi/<module>-api.yaml
```

## Sub-Skills

| Topic | File |
|-------|------|
| Path standards | [api-path-standard-skill.md](api-path-standard-skill.md) |
| Schemas | [request-response-schema-skill.md](request-response-schema-skill.md) |
| Status codes | [http-status-code-skill.md](http-status-code-skill.md) |
| Pagination | [pagination-filtering-sorting-skill.md](pagination-filtering-sorting-skill.md) |
| Contract review | [openapi-contract-review-skill.md](openapi-contract-review-skill.md) |

## Template

See [../templates/openapi-template.yaml](../templates/openapi-template.yaml).
