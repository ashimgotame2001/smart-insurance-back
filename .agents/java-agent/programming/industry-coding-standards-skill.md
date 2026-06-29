# Industry Coding Standards Skill

## Purpose

Ensure all generated and reviewed Java code meets enterprise-grade quality — readable, maintainable, testable, and aligned with widely adopted industry practices for Spring Boot backends.

## When to Use

- Before and during Phase 4 (Generate) in [end-to-end-workflow.md](../end-to-end-workflow.md)
- When reviewing or refactoring existing SR code
- When the user asks for production-ready or industry-standard code

## Core Principles

Apply these in every class and method:

| Principle | Practice in SR |
|-----------|----------------|
| **SOLID — SRP** | One reason to change per class: controller = HTTP, service = orchestration, validation service = rules, mapper = mapping |
| **SOLID — OCP** | Extend via Strategy/adapters in Solution layer; avoid modifying Product core for tenant variants |
| **SOLID — LSP** | Strategy and port implementations must honor interface contracts without surprising callers |
| **SOLID — ISP** | Small focused interfaces (`XxxPort`, `XxxStrategy`); no fat god-interfaces |
| **SOLID — DIP** | Services depend on repository interfaces and outbound ports, not concrete adapters |
| **DRY** | Reuse Platform utilities, mappers, and shared validators — do not duplicate cross-cutting logic |
| **KISS** | Smallest complete vertical slice; no speculative abstractions |
| **YAGNI** | Add patterns only when the feature requires them — see [design-pattern-selection-skill.md](design-pattern-selection-skill.md) |

## Java & Spring Boot Standards

### Naming and structure

- **Classes:** PascalCase nouns (`RemittanceValidationService`, `PaymentClientAdapter`)
- **Methods:** camelCase verbs (`createRemittance`, `validateRequest`)
- **Constants:** `UPPER_SNAKE_CASE`
- **Packages:** lowercase, layer suffix (`.api`, `.application`, `.domain`, `.infrastructure`)
- **One public top-level type per file**; package matches directory layout

### Code style

- Constructor injection only — no field `@Autowired`
- `final` fields for dependencies and immutable locals where possible
- No wildcard imports; explicit imports only
- Prefer `Optional` for single nullable returns from repositories — never return `null` from public service APIs
- Use `record` for immutable DTOs/value objects when appropriate
- Guard clauses and early returns over deep nesting
- Methods ≤ ~30 lines; extract private helpers when logic grows
- No magic numbers or strings — use enums, constants, or message codes

### Spring conventions

- `@Transactional` on service **write** methods only; `readOnly = true` for queries
- Controllers return `ResponseEntity<GlobalResponse<T>>`; services return `GlobalResponse<T>`
- Validation: Jakarta Validation on DTOs + business rules in `XxxValidationService`
- Never expose JPA entities in API layer — map to response models
- Configuration via `@ConfigurationProperties` or externalized properties — no hardcoded env values

### Error handling

- Throw `GlobalException` with stable message codes — see [exception-handling-skill.md](exception-handling-skill.md)
- Fail fast on invalid input in validation layer
- Do not swallow exceptions; log with context and rethrow or map to domain errors
- Use specific exception types over generic `RuntimeException`

### Logging and observability

- Structured logging with correlation/trace IDs — see [observability-skill.md](observability-skill.md)
- Log at appropriate levels: `INFO` for business milestones, `DEBUG` for diagnostics, `WARN`/`ERROR` for failures
- Never log PII, credentials, tokens, or full request bodies with sensitive fields

### Security

- Validate and sanitize all external input
- Enforce authorization at controller or service boundary — see [security-skill.md](security-skill.md)
- No secrets in source code; use Platform security context for current user

### Testing standards

- Arrange–Act–Assert structure in unit tests
- Mock external ports/adapters; use `@DataJpaTest` / `@WebMvcTest` for slice tests
- Test happy path, validation failures, and key business-rule errors
- Meaningful test names: `shouldRejectRemittanceWhenAmountExceedsLimit`

### Documentation

- JavaDoc on public service methods with requirement/traceability ID
- OpenAPI descriptions for every endpoint, parameter, and response schema
- Document non-obvious business rules inline or link to FRD

## Pre-Delivery Quality Gate

Before marking code complete, verify:

- [ ] SOLID layering respected — no business logic in controllers
- [ ] Constructor injection, no field injection
- [ ] Entities never leak to API responses
- [ ] Exceptions use Platform types and stable codes
- [ ] Logging includes trace context; no sensitive data logged
- [ ] Tests cover orchestration and validation paths
- [ ] Code matches existing module conventions
- [ ] Required patterns applied by category (Creational / Structural / Behavioral) for the feature type — see [design-pattern-selection-skill.md](design-pattern-selection-skill.md)

## Related

- [design-pattern-selection-skill.md](design-pattern-selection-skill.md)
- [platform-standard-usage-skill.md](platform-standard-usage-skill.md)
- [spring-boot-layered-architecture-skill.md](spring-boot-layered-architecture-skill.md)
- [../code-generation/code-generation-output-standard.md](../code-generation/code-generation-output-standard.md)
- [../prompts/industry-standard-code-generation.md](../prompts/industry-standard-code-generation.md)
