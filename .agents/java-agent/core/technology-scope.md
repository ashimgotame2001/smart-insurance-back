# Technology Scope

## Core Stack

| Technology | Usage |
|------------|-------|
| Java 25 | Language baseline |
| Spring Boot 4.x | Application framework |
| Spring Data JPA | Persistence |
| Spring Data Projections | List/summary read queries |
| PostgreSQL 18 | Primary relational database |
| Jakarta Validation | Request and domain validation |
| Spring Security | Authentication and authorization |
| OpenAPI 3.x | API contract definition |
| MapStruct | **Primary** — DTO ↔ entity mapping; inject `XxxMapper` directly in service |
| ModelMapper | Secondary — simple/temporary direct mapping when MapStruct not yet defined |
| Gradle | Multi-module build |

## Integration Stack

| Technology | Usage |
|------------|-------|
| REST | Primary external API style |
| WebClient | Outbound HTTP to external/partner systems |
| gRPC | Internal high-performance service calls |
| Kafka / Event Bus | Async domain events |
| Apache Camel 4.x | Enterprise integration routes (`edx-enterprise-integration`) |
| JasperReports 7.x | PDF/HTML/Excel report generation (`edx-reporting-engine`) |
| Micrometer | Metrics |
| Structured logging (JSON) | Log aggregation |
| OpenTelemetry / tracing | Distributed trace propagation |

## Conventions

- Use constructor injection; avoid field injection
- Prefer immutable classes or DTOs where appropriate
- Use `@Transactional` at service layer only
- Externalize configuration via Spring `@ConfigurationProperties`
- Use Platform-provided base classes for pagination, exceptions, and audit fields

## Not in Scope Unless Requested

- Reactive stack (WebFlux) for standard CRUD APIs
- NoSQL databases
- Custom ORM frameworks outside JPA
