# Adapter Generation Skill

## Purpose

Generate inbound REST adapters (controllers) that expose OpenAPI-defined APIs and return **`GlobalResponse`**.

**Reference:** `com.swifttech.edx.dm.bucket.adapter.BucketController`

## Rules

- Implement OpenAPI interface when generated
- `@RestController` + `@RequestMapping` base path from spec
- Inject **service interface** (`XxxService`), not implementation
- Accept `@Valid` request models
- Return **`ResponseEntity<GlobalResponse<T>>`** — delegate to service
- Declare `throws GlobalException` on handler methods
- No business logic in adapter — delegate to service
- When service returns plain data (list/model), wrap with `MessageHelper.buildSuccessResponseWithData` in adapter

## Template

See [../templates/controller-template.md](../templates/controller-template.md).

## Checklist

- [ ] Matches OpenAPI operationId
- [ ] Returns `ResponseEntity<GlobalResponse<...>>`
- [ ] Service interface injected
- [ ] Pagination params on list endpoints
- [ ] Security annotation if required
- [ ] `throws GlobalException` declared
