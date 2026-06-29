# Projection Skill

## Purpose

Use Spring Data JPA projections to fetch only the columns needed for read and list operations instead of loading full entities.

## When to Use

| Scenario | Use projection |
|----------|----------------|
| Paginated list / table API | Yes — return summary projection |
| Search results | Yes |
| Dropdown / lookup options | Yes |
| Read by ID with full detail | Full entity or wide projection |
| Write / state change flows | Full entity |

## Projection Types

| Type | Location | Best for |
|------|----------|----------|
| Interface (closed) | `domain.projection` | Fixed field subset aligned with list API |
| Class DTO | `domain.projection` | Multi-field summaries, constructor expressions |
| Dynamic projection | Repository method param | Reusable query, varying field sets (rare) |

## Package Convention

```
domain/
├── entity/
├── repository/
└── projection/
    └── RemittanceSummaryProjection.java
```

## Interface Projection Pattern

```java
public interface RemittanceSummaryProjection {
    Long getUid();
    String getStatus();
    BigDecimal getAmount();
    String getCurrency();
    Instant getCreatedAt();
}
```

```java
Page<RemittanceSummaryProjection> findByStatus(String status, Pageable pageable);
```

## Rules

- Align projection fields with OpenAPI list/summary schema (`*Summary` models)
- Use projections for **list** repository methods by default
- Use `@Query` with `select new ...` only when interface projection is insufficient
- Do not expose projections directly from controllers — map to API response models in the service when shapes differ
- Index columns used in projection filters and sort
- Prefer closed projections; avoid open projections unless justified

## Service Flow

```
Repository → Page<Projection> → map to PageResponse<SummaryModel> → adapter response
```

Use MapStruct or manual mapping when projection fields do not match API model names.

## Anti-Patterns

| Pattern | Why |
|---------|-----|
| `Page<Entity>` for list APIs exposing 5 fields | Loads unused columns and risks lazy-init issues |
| Projection with entity associations | Can trigger N+1; project scalar fields only |
| Business logic on projection interface | Keep projections read-only |

## Related

- [spring-data-jpa-skill.md](spring-data-jpa-skill.md)
- [performance-skill.md](performance-skill.md)
- [../code-generation/projection-generation-skill.md](../code-generation/projection-generation-skill.md)
