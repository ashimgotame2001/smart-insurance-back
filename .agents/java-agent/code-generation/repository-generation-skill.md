# Repository Generation Skill

## Rules

- Interface in `domain.repository`
- JPA impl in `infrastructure.persistence` extending `JpaRepository`
- Custom finder methods follow Spring Data naming conventions
- Complex queries: `@Query` or `Specification`
- **List methods:** return `Page<Projection>` for summary APIs ([projection-generation-skill.md](projection-generation-skill.md))
- Return `Optional<Entity>` for detail loads; `Optional<Projection>` when detail is read-only and narrow

## Template

See [../templates/repository-template.md](../templates/repository-template.md).
