# Projection Generation Skill

## Purpose

Generate Spring Data JPA projections for list and read-only query endpoints.

## Deliverables

1. Projection interface or class in `domain.projection`
2. Repository method returning `Page<Projection>` or `Optional<Projection>`
3. Service mapping from projection to API summary model
4. OpenAPI `*Summary` schema aligned with projection fields

## Naming

| Artifact | Pattern |
|----------|---------|
| List projection | `{Resource}SummaryProjection` |
| Repository method | `findSummariesBy...`, `searchSummariesBy...` |
| API model | `{Resource}Summary` |

## Generation Rules

- One projection per list/summary API shape
- Field getters match entity property paths (closed interface projection)
- Include only fields required by UI table or OpenAPI summary schema
- Add filter/sort parameters to repository method, not projection
- For PostgreSQL 18, ensure projected filter columns are indexed

## Checklist

- [ ] Projection in `domain.projection`
- [ ] List repository returns `Page<Projection>`, not `Page<Entity>`
- [ ] OpenAPI summary schema matches projection fields
- [ ] Service maps projection page to Platform page response
- [ ] No lazy associations on projection

## Template

See [../templates/projection-template.md](../templates/projection-template.md).

## Related

- [../programming/projection-skill.md](../programming/projection-skill.md)
