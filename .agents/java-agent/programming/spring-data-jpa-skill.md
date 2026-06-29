# Spring Data JPA Skill

## Entity Rules

- Use `@Entity`, explicit `@Table(name = "...")`
- Extend **`com.swifttech.edx.dm.entity.BaseEntity`** — provides `uid` (`@Id`), `version`, audit columns
- Do not declare `@Id` or `uid` on concrete entities
- `JpaRepository<Entity, Long>` — `Long` is the `uid` type
- Use `@Enumerated(EnumType.STRING)` for status fields
- Lazy collections by default; avoid `EAGER` unless justified

## Repository Rules

- Extend `JpaRepository<Entity, Id>` or custom interface
- Complex filters: `JpaSpecificationExecutor` or `@Query`
- **List queries:** return `Page<Projection>` when API exposes a summary subset ([projection-skill.md](projection-skill.md))
- **Detail queries:** return `Optional<Entity>` or full projection as needed
- No business logic in repository methods

## Transactions

- `@Transactional` on service layer, read-only for queries
- Avoid `EntityManager` in controllers

## Naming

| Type | Pattern |
|------|---------|
| Entity | `RemittanceTransaction` |
| Repository | `RemittanceTransactionRepository` |
| Custom query method | `findByStatusAndCustomerId` |

## Performance

- Index columns used in filter/sort
- Avoid N+1: use projections, `@EntityGraph`, or fetch join where needed
- Do not expose `Page<Entity>` to API for list endpoints; use projections and map to response models

See [projection-skill.md](projection-skill.md) and [../review/jpa-review.md](../review/jpa-review.md).
