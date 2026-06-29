# Service Generation Skill

## Purpose

Generate application services using the **interface + implementation** pattern with **`GlobalResponse`** return type and **separation of concerns**: validation layer → mapping (MapStruct direct or custom mapping service) → persistence.

**Reference:** `com.swifttech.edx.dm.bucket.service` (`BucketService`, `BucketServiceImpl`)

## Deliverables

1. `XxxService` interface in `application` or `service` package
2. `XxxServiceImpl` in `application/impl` or `service/impl` — **orchestrator only**
3. `XxxValidationService` in `validator` package — business rule validation (always)
4. `XxxMapper` (MapStruct) in `mapper` package — direct inject for standard mapping (always for REST aggregates)
5. `XxxMappingService` in `mapper` package — **optional**, custom mapping only
6. Methods return `GlobalResponse<T>` for REST-exposed operations
7. Optional infrastructure port interface (e.g. `StorageService`) for external systems

## Separation of Concerns

| Layer | Class | When |
|-------|-------|------|
| Validation | `CustomerValidationService` | Always |
| Direct mapping | `CustomerMapper` (MapStruct) | Default — inject directly, no wrapper |
| Custom mapping | `CustomerMappingService` | Only when MapStruct/ModelMapper insufficient |
| Persistence | `CustomerRepository` | Always |

See [../programming/service-layer-separation-skill.md](../programming/service-layer-separation-skill.md).

## Rules

- **Interface + impl** — controller injects `XxxService`, not `XxxServiceImpl`
- `@Service` on implementation and validation service classes
- `@Transactional` on impl write methods; `readOnly = true` on queries
- Inject **`XxxValidationService`**, **`XxxMapper`** (MapStruct), repository; optional **`XxxMappingService`** for custom mapping
- Return **`GlobalResponse<T>`** built via `MessageHelper` or `ServiceResponseBuilder`
- Throw **`GlobalException`** from validation service — do not return fail `GlobalResponse` manually for exceptions
- Success codes use **100–199** range per [message-code-convention-skill.md](../programming/message-code-convention-skill.md)
- Publish events after successful save when applicable

## Command Method Structure

1. **`xxxValidationService.createValidate(request)`** — business rules
2. **`xxxMapper.toEntity(request)`** — MapStruct direct (or `mappingService.toCreateEntity` if custom)
3. **`repository.save(entity)`** — persist
4. Publish events (if any)
5. **`xxxMapper.toResponse(saved)`** — MapStruct direct (or `mappingService.toEnrichedResponse` if custom)
6. **`MessageHelper.buildSuccessResponseWithData(code, data)`**

## Update Method Structure

1. Load entity (throw `GlobalException` if not found)
2. **`xxxValidationService.updateValidate(request, entity)`**
3. **`xxxMapper.updateEntity(request, entity)`**
4. **`repository.save(entity)`**
5. Map to response → `GlobalResponse`

## Return Type Guide

| Operation | Service return | Adapter return |
|-----------|----------------|----------------|
| Create / update / get | `GlobalResponse<CustomerResponse>` | `ResponseEntity<GlobalResponse<CustomerResponse>>` |
| Paginated list | `GlobalResponse<PageResponse>` | `ResponseEntity<GlobalResponse<?>>` |
| Bulk / list built in service | `List<DocumentResponse>` | Wrap with `MessageHelper.buildSuccessResponseWithData` in adapter |
| File/stream download | `BucketResponse` (headers + resource) | `ResponseEntity<InputStreamResource>` |

## Templates

- [../templates/service-interface-template.md](../templates/service-interface-template.md)
- [../templates/service-template.md](../templates/service-template.md)
- [../templates/validation-service-template.md](../templates/validation-service-template.md)

## Related

- [adapter-generation-skill.md](adapter-generation-skill.md)
- [validator-generation-skill.md](validator-generation-skill.md)
- [mapper-generation-skill.md](mapper-generation-skill.md)
- [../programming/model-mapper-skill.md](../programming/model-mapper-skill.md)
- [../programming/exception-handling-skill.md](../programming/exception-handling-skill.md)
