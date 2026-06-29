# Platform Standard Usage Skill

## Purpose

Reuse Platform-layer utilities instead of reimplementing cross-cutting concerns.

## Use Platform For

| Concern | Platform utility |
|---------|------------------|
| Pagination | `PageRequest`, `PageResponse` wrappers |
| Errors | `GlobalException`, `GlobalResponse`, `GlobalExceptionHandlerBaseController` |
| Success responses | `MessageHelper.buildSuccessResponse*`, `ServiceResponseBuilder` |
| Base entity | `com.swifttech.edx.dm.entity.BaseEntity` — `uid`, `version`, audit fields |
| Security context | Current user/principal accessor |
| Tracing | MDC correlation ID propagation |
| Date/time | UTC `Clock` bean |

## Rules

1. Search Platform modules before creating a new utility class
2. Extend `BaseEntity` for entities; throw `GlobalException` for errors
3. Do not copy-paste Platform code into Product modules
4. Configure Platform beans via shared auto-configuration

## When Product Needs Custom Behavior

- Extend Platform class in Product module only if remittance-specific
- Do not modify Platform module from Product code
