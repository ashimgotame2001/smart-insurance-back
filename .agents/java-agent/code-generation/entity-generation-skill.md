# Entity Generation Skill

## Rules

- JPA entity in `domain.entity`
- **Extend `com.swifttech.edx.dm.entity.BaseEntity`** — inherited fields:
  - `uid` (`@Id`, `Long`, `IDENTITY`) — do not redeclare
  - `version` (`@Version`)
  - `createdAt`, `lastModifiedAt`, `createdBy`, `lastModifiedBy` (auditing)
- Do **not** add `@Id`, `id`, or `uid` fields on concrete entities
- Explicit `@Table`, `@Column` for domain-specific columns only
- Relationships with correct cascade and fetch type
- Status as `@Enumerated(EnumType.STRING)`
- `JpaRepository<Entity, Long>` — `Long` refers to `uid`

## Template

See [../templates/entity-template.md](../templates/entity-template.md).

## Related

- [../programming/platform-standard-usage-skill.md](../programming/platform-standard-usage-skill.md)
- [../programming/spring-data-jpa-skill.md](../programming/spring-data-jpa-skill.md)
