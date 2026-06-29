# Design Pattern Selection Skill

## Purpose

Select and apply the **minimum required design pattern(s)** for each feature type in Smart Remittance — industry-standard GoF patterns (Creational, Structural, Behavioral) mapped to SR architecture, not pattern overuse.

## When to Use

- During Phase 3 (Design) and Phase 4 (Generate)
- When choosing between inline logic vs. a structured pattern
- When reviewing whether existing code uses the correct pattern for its concern

## Selection Rule

1. Identify the **primary feature type** from the capability (CRUD, integration, event, tenant variant, etc.)
2. Classify required patterns by **category**: Creational → Structural → Behavioral
3. Apply the **required pattern(s)** from the maps below — baseline + feature-specific only
4. Do **not** add extra patterns unless the feature complexity demands it
5. Document pattern category, name, and implementing class in the implementation summary

## Systematic Pattern Selection

For every feature, evaluate in this order:

```
1. Creational  — how are objects/DTOs/beans constructed?
2. Structural  — how are layers, adapters, and boundaries composed?
3. Behavioral  — how does runtime logic, flow, and variation work?
```

| Category | Question to Ask | SR Default |
|----------|-----------------|------------|
| **Creational** | Does construction involve branching, many optional fields, or variant families? | Spring DI + MapStruct; add Factory/Builder only when needed |
| **Structural** | Does the feature cross boundaries (HTTP, DB, external API, events)? | Layered Architecture + Repository + DTO/Mapper |
| **Behavioral** | Does behavior vary, follow a workflow, or notify observers? | ValidationService orchestration; add Strategy/Observer/State when needed |

---

## Creational Patterns

Use when object **construction** is non-trivial. Prefer Spring DI for wiring; add explicit creational patterns only when construction logic has branching or complexity.

| Pattern | Use When (Feature Requirement) | SR Implementation | Do Not Use When |
|---------|-------------------------------|-------------------|-----------------|
| **Singleton** | Single shared instance needed app-wide | Spring default scope (`@Service`, `@Component`) — never manual singleton | Simple CRUD with no shared state |
| **Factory Method** | One product type, creation varies by input/config | `XxxFactory.create(context)` or `@Configuration` `@Bean` factory method | Single construction path with no branching |
| **Abstract Factory** | Families of related objects must stay consistent (e.g. partner-specific client + mapper + config) | `PartnerIntegrationFactory` returning port + DTO mapper + config bundle in Solution module | One-off object creation; simple CRUD |
| **Builder** | Many optional fields; stepwise or readable assembly | Lombok `@Builder` on request/response DTOs; builder for complex domain aggregates | Flat DTOs with ≤5 fields — use constructor or record |
| **Prototype** | Copy existing object as template with minor changes | `entity.toBuilder()` / manual copy for audit snapshots or draft clones | Standard entity create/update flows |

### Creational — Feature Triggers

| Feature / Scenario | Required Creational Pattern(s) |
|--------------------|-------------------------------|
| REST CRUD with flat DTOs | Spring DI only (implicit Factory via container) |
| Complex create request (many optional fields) | **Builder** on request DTO |
| Aggregate built from multiple repositories/services | **Factory Method** — `RemittanceAggregateFactory` |
| Partner-specific object families (client + mapper + config) | **Abstract Factory** in Solution module |
| Clone entity for revision/draft | **Prototype** (copy method) |

---

## Structural Patterns

Use when composing **layers, boundaries, and integrations**. These are the most common in SR backends.

| Pattern | Use When (Feature Requirement) | SR Implementation | Do Not Use When |
|---------|-------------------------------|-------------------|-----------------|
| **Adapter** | Interface mismatch with external system or API version | `XxxClientAdapter` implements `XxxPort`; version-specific DTO adapters | Internal module calls with matching interfaces |
| **Facade** | Simplify complex subsystem for callers | Service layer as facade: `RemittanceService` orchestrates validation, repo, adapters | Controller doing orchestration — keep facade in application layer |
| **Decorator** | Add cross-cutting behavior without changing core class | Platform resilience wrappers (retry/timeout on WebClient); AOP for audit/logging | Duplicating logic in every adapter manually |
| **Proxy** | Control access, lazy load, or transactional boundary | Spring `@Transactional` proxy; JPA lazy associations | Eager-loading everything or manual proxy classes |
| **Bridge** | Abstraction and implementation vary independently | Port interface (abstraction) + Adapter impl (implementation) for outbound calls | Single fixed implementation with no variation |
| **Composite** | Tree/hierarchy operations treated uniformly | Hierarchical fee/rule trees, org structures | Flat entities with no parent-child logic |
| **Flyweight** | Many objects share immutable state | Shared cached config/reference data (corridor codes, currency metadata) | Mutable per-request state |

### Structural — Feature Triggers

| Feature / Scenario | Required Structural Pattern(s) |
|--------------------|-------------------------------|
| REST CRUD API | **Layered Architecture**, **Repository**, **DTO + Mapper**, **Facade** (service) |
| Outbound REST/gRPC | **Adapter** + **Bridge** (port/abstraction) + Anti-Corruption Layer |
| External model isolation | **Adapter** (map external → internal DTOs inside adapter only) |
| API versioning / backward compat | **Adapter** at API boundary |
| Cross-cutting audit/trace/resilience | **Decorator** via Platform AOP/resilience utilities |
| Complex subsystem (multi-repo + multi-adapter) | **Facade** in application service |
| List/search with dynamic filters | **Repository** + Projection; **Composite** `Specification` when criteria combine |

---

## Behavioral Patterns

Use when **runtime behavior, flow, or variation** drives the design.

| Pattern | Use When (Feature Requirement) | SR Implementation | Do Not Use When |
|---------|-------------------------------|-------------------|-----------------|
| **Strategy** | Algorithm/behavior varies by tenant, config, or rule | `FeeCalculationStrategy`, `@ConditionalOnProperty` impls | Single fixed algorithm |
| **Observer** | State change must notify other components async | Domain events + `@EventListener` / Kafka publish-consume | Synchronous-only CRUD with no downstream reaction |
| **Template Method** | Shared workflow skeleton, steps vary in subclasses | Abstract base in reporting/validation pipeline; Jasper pipeline | One-off method with no shared steps |
| **State** | Object behavior changes with lifecycle status | Remittance status transitions (`Draft` → `Submitted` → `Settled`) | Simple boolean flags with no state-specific rules |
| **Command** | Encapsulate operation for queue, audit, or undo | Command DTO + handler for async job processing | Direct service method call suffices |
| **Chain of Responsibility** | Multiple handlers process request sequentially | Validation chain; Camel route steps; servlet/filter chain via Platform | Single validation service covers all rules |
| **Mediator** | Many components interact — reduce direct coupling | Spring `ApplicationEventPublisher`; orchestration service | Direct injection between two classes is enough |
| **Iterator** | Traverse collection without exposing internals | Java `Stream`, `Page<T>`, repository pagination | N/A — use standard Java collections |
| **Specification** | Composable business rules for query/filter | Spring Data `Specification<T>` for dynamic list filters | Static `@Query` covers all cases |
| **Memento** | Capture/restore object state | Audit snapshots, draft/version restore | Simple update with no history requirement |
| **Visitor** | Operations vary across object hierarchy | Rare — hierarchical rule evaluation trees | Flat domain with no type hierarchy |

### Behavioral — Feature Triggers

| Feature / Scenario | Required Behavioral Pattern(s) |
|--------------------|-------------------------------|
| Business rule validation | Dedicated **ValidationService** (separation of concerns) |
| Tenant/solution-specific behavior | **Strategy** (+ Factory or `@Primary` override) — [service-runtime-switching-skill.md](service-runtime-switching-skill.md) |
| Runtime algorithm swap (fees, routing) | **Strategy** |
| Async domain notification | **Observer** (event-driven) — [event-driven-skill.md](event-driven-skill.md) |
| Idempotent message consumer | **Idempotent Consumer** (behavioral idiom) |
| Multi-step partner flow | **Chain of Responsibility** / Orchestration — [camel-route-skill.md](camel-route-skill.md) |
| Lifecycle/workflow (status-driven rules) | **State** |
| Dynamic list/filter queries | **Specification** |
| Report generation pipeline | **Template Method** — [jasper-reporting-skill.md](jasper-reporting-skill.md) |
| Async job / queued operation | **Command** + handler |

---

## Feature → Required Pattern Map (All Categories)

| Feature / Scenario | Creational | Structural | Behavioral | SR Implementation |
|--------------------|------------|------------|------------|-------------------|
| REST CRUD API | Spring DI | Layered, Repository, DTO/Mapper, Facade | ValidationService | Controller → Service → Repository; MapStruct; `GlobalResponse` |
| Complex create request | Builder | Layered, Repository | ValidationService | `@Builder` request DTO; validate → map → save |
| Business rule validation | — | Facade (validation service boundary) | ValidationService, Specification (rules) | `XxxValidationService` + Jakarta Validation |
| List/search with filters | — | Repository, Projection | Specification | Spring Data projection; composable `Specification` |
| Outbound REST/gRPC | Factory Method (client bean) | Adapter, Bridge (port) | — | `XxxPort` + `XxxClientAdapter` |
| Multi-step partner flow | — | Adapter | Chain of Responsibility, Mediator | Camel route orchestration |
| Tenant/solution variant | Abstract Factory (optional) | Bridge | Strategy | [service-runtime-switching-skill.md](service-runtime-switching-skill.md) |
| Runtime algorithm swap | — | — | Strategy | Interface in Product; impls in Product/Solution |
| Async notification | — | — | Observer | Domain event + `@EventListener` / Kafka |
| Idempotent consumer | — | — | Idempotent Consumer | Dedup key before side effects |
| Object creation with variants | Factory Method / Abstract Factory | — | Strategy (variant selection) | `XxxFactory` when construction branches |
| Cross-cutting audit/trace | — | Decorator, Proxy | — | Platform AOP, MDC, `BaseEntity` |
| External model isolation | — | Adapter (ACL) | — | Map external payloads inside adapter only |
| Report generation | — | Facade | Template Method | Jasper pipeline |
| API versioning | — | Adapter | — | Version-specific DTOs + mapper |
| Lifecycle/status workflow | — | — | State | Status enum + state-specific validation/rules |

---

## Pattern Application Rules

### Always (baseline for every feature)

| Category | Patterns |
|----------|----------|
| Creational | Spring DI (constructor injection) |
| Structural | Layered Architecture, Repository, DTO + Mapper, Facade (service) |
| Behavioral | ValidationService separation for business rules |

### Apply when feature type matches

| Category | Apply When |
|----------|------------|
| Creational — Builder | DTO/aggregate has many optional fields or stepwise assembly |
| Creational — Factory / Abstract Factory | Construction branches or partner-specific object families |
| Structural — Adapter | External system call or API version mismatch |
| Structural — Decorator | Cross-cutting resilience/audit without modifying core |
| Behavioral — Strategy | Behavior varies by tenant, config, or pluggable algorithm |
| Behavioral — Observer | Downstream async reaction to state changes |
| Behavioral — State | Lifecycle status drives different rules/behavior |
| Behavioral — Specification | Dynamic composable query/filter criteria |
| Behavioral — Template Method | Shared multi-step pipeline with varying steps |

### Never

- Manual **Singleton** — use Spring bean scope
- **Abstract Factory** for simple CRUD or single-object creation
- **Strategy** for a single fixed implementation
- **State** for simple boolean flags without status-specific behavior
- **Visitor** / **Memento** unless audit/hierarchy requirements explicitly demand them
- God service combining validation, mapping, HTTP, and persistence

---

## Decision Flow

```
Feature identified
    │
    ├─ CREATIONAL: Complex construction?
    │       ├─ Many optional fields ──► Builder
    │       ├─ Branching creation logic ──► Factory Method
    │       └─ Partner object families ──► Abstract Factory
    │
    ├─ STRUCTURAL: Crosses a boundary?
    │       ├─ External system ──► Adapter + Port (Bridge)
    │       ├─ API version mismatch ──► Adapter
    │       ├─ Complex subsystem ──► Facade (service layer)
    │       └─ Cross-cutting concern ──► Decorator (Platform AOP)
    │
    ├─ BEHAVIORAL: Runtime variation or flow?
    │       ├─ Tenant/config algorithm swap ──► Strategy
    │       ├─ Status-driven rules ──► State
    │       ├─ Async downstream reaction ──► Observer (events)
    │       ├─ Multi-step pipeline ──► Chain / Template Method / Camel
    │       ├─ Dynamic filters ──► Specification
    │       └─ Queued/audited operation ──► Command
    │
    └─ Standard CRUD ──► Baseline: Spring DI + Layered + Repository + Mapper + Facade + ValidationService
```

---

## Output

When generating code, state for each capability:

1. **Feature type** identified
2. **Pattern matrix** — category → pattern name → implementing class

Example:

| Category | Pattern | Class |
|----------|---------|-------|
| Creational | Builder | `CreateRemittanceRequest` |
| Structural | Adapter, Facade | `PaymentClientAdapter`, `RemittanceService` |
| Behavioral | Strategy, Observer | `StandardFeeStrategy`, `RemittanceCreatedEvent` |

---

## Related

- [industry-coding-standards-skill.md](industry-coding-standards-skill.md)
- [spring-boot-layered-architecture-skill.md](spring-boot-layered-architecture-skill.md)
- [../code-generation/adapter-design-pattern-skill.md](../code-generation/adapter-design-pattern-skill.md)
- [service-runtime-switching-skill.md](service-runtime-switching-skill.md)
- [event-driven-skill.md](event-driven-skill.md)
- [../prompts/industry-standard-code-generation.md](../prompts/industry-standard-code-generation.md)
