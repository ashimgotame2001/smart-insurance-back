# Spring Boot Layered Architecture Skill

## Package Layout

```
com.edx.platform.sr.<module>
├── api
├── application
│   ├── RemittanceService.java          # interface
│   └── impl/
│       └── RemittanceServiceImpl.java
├── model
│   ├── request
│   └── response
├── domain
│   ├── entity
│   ├── projection
│   └── repository
├── infrastructure
│   ├── persistence
│   ├── adapter          # inbound REST controllers
│   └── event
├── mapper
├── validator/              # XxxValidationService + Jakarta @Constraint validators
└── config
```

## Dependency Direction

```
api → application → domain ← infrastructure
```

- `domain` must not depend on `api` or Spring Web
- `infrastructure` implements `domain.repository` interfaces

## Controller Pattern

- Implement OpenAPI-generated interface when available
- Inject **service interface**; return `ResponseEntity<GlobalResponse<T>>`
- Method body: validate → call service → return `ResponseEntity.ok(serviceResult)`
- Declare `throws GlobalException`; no local exception handling

## Service Pattern

- **`XxxService` interface** + **`XxxServiceImpl`** in `application/impl`
- REST-facing methods return **`GlobalResponse<T>`** via `MessageHelper`
- Service impl is an **orchestrator** — inject `XxxValidationService` + `XxxMapper` + repository
- Command flow: **`validationService.createValidate(request)` → `mapper.toEntity` → `repository.save`**
- Infrastructure ports (e.g. `StorageService`) return plain types for internal use
- `@Transactional` on impl write methods

See [service-layer-separation-skill.md](service-layer-separation-skill.md).

**Reference:** `com.swifttech.edx.dm.bucket`

## Repository Pattern

- Interface in `domain.repository`
- JPA implementation in `infrastructure.persistence`
- Custom queries via `@Query` or Specification for complex filters
