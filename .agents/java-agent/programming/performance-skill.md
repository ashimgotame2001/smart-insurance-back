# Performance Skill

## Purpose

Design APIs and persistence for acceptable latency under remittance load.

## Guidelines

- Paginate all list endpoints; default page size 20, max 100
- Use indexes on filter and sort columns
- Avoid N+1 queries; batch fetch or use projections where needed
- Use JPA projections for paginated list APIs ([projection-skill.md](projection-skill.md))
- Cache reference data (corridors, currencies) with TTL
- Async processing for non-critical path (notifications, reporting)
- Set WebClient timeouts and connection pool limits

## Anti-Patterns

- Unbounded `findAll()` exposed via API
- Synchronous call chains across multiple external systems
- Loading full entity graphs for list views
- `Page<Entity>` on list APIs when a summary projection is sufficient

See [projection-skill.md](projection-skill.md) and [../review/performance-review.md](../review/performance-review.md).
