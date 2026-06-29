# Code Generation Output Standard

## Purpose

Define consistent structure and quality bar for all generated Java code.

## Output Package

For each feature, deliver:

1. OpenAPI YAML diff or new file
2. Entity + repository + projection (for list APIs)
3. Request/response models
4. MapStruct `XxxMapper` (+ optional `XxxMappingService` for custom mapping)
5. DTO validators + `XxxValidationService`
6. Service interface + implementation (validate → map → save)
7. Inbound adapter (controller)
8. Event classes + publisher/handler if applicable
9. Outbound adapter if external integration
10. Unit tests for validation service and service impl orchestration
11. Integration tests for REST, JPA, or messaging when behavior spans layers ([integration-test-generation-skill.md](integration-test-generation-skill.md))
12. Traceability matrix snippet

## Code Quality Bar

- Follow [industry-coding-standards-skill.md](../programming/industry-coding-standards-skill.md) — SOLID, clean code, enterprise Java/Spring conventions
- Apply feature-appropriate patterns per [design-pattern-selection-skill.md](../programming/design-pattern-selection-skill.md)
- Constructor injection only
- No wildcard imports
- JavaDoc on public service methods with requirement ID
- Follow existing target module package layout ([spring-boot-layered-architecture-skill.md](../programming/spring-boot-layered-architecture-skill.md))
- Use Platform base types

## Templates

Use files in [../templates/](../templates/) — do not invent alternate patterns without justification.

## Sub-Skills

See `code-generation/*-generation-skill.md` for layer-specific rules, including [integration-test-generation-skill.md](integration-test-generation-skill.md).
