# Architecture Approach

## Layered Flow

```
Client → Controller (api) → Service (application) → Repository (domain/infrastructure) → Database
                ↓
         DTO / Mapper / Validator
                ↓
         Events / Adapters (infrastructure)
```

## Responsibilities by Layer

| Layer | Package | Does | Does Not |
|-------|---------|------|----------|
| API | `api/` | HTTP mapping, request binding, response status | Business logic, direct DB access |
| Application | `application/` | Use cases, orchestration, transactions | HTTP concerns, JPA queries |
| Domain | `domain/` | Entities, domain rules, repository interfaces | Spring web annotations |
| Infrastructure | `infrastructure/` | JPA implementations, adapters, event handlers | API exposure |

## Cross-Cutting Concerns

Handled via Platform modules or Spring filters:

- Authentication / authorization
- Request logging and correlation IDs
- Exception translation to standard error responses
- Audit fields (`createdBy`, `createdAt`, etc.)
- Pagination and sorting utilities

## Event-Driven Integration

- Publish domain events after successful state changes
- Consume events in dedicated handlers; keep handlers thin
- Use idempotent consumers where delivery is at-least-once

## Multi-Module Rules

1. Product modules expose stable application interfaces
2. Solution modules override behavior via runtime switching or adapters
3. Depend on interfaces, not concrete infrastructure from other modules

See [platform-product-solution-boundary.md](platform-product-solution-boundary.md) for placement rules.
