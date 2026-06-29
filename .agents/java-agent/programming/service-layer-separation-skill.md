# Service Layer Separation Skill

## Purpose

Keep **`XxxServiceImpl`** thin by delegating to dedicated, injectable layers:

1. **Validation layer** — `XxxValidationService` (always — business rules, state guards, uniqueness)
2. **Mapping** — MapStruct `XxxMapper` injected **directly** for standard mapping; optional `XxxMappingService` only for custom transforms
3. **Service impl** — orchestrates validate → map → persist → map → respond

Do not embed validation logic or inline field mapping inside service method bodies.

## When to Use

- Every command method: `create`, `update`, `delete`, state transitions
- Query methods when existence or access preconditions apply before load

## Layer Responsibilities

| Layer | Class | Required | Responsibility |
|-------|-------|----------|----------------|
| API boundary | Jakarta `@Valid` + `@Constraint` | Yes | Field format, required fields, cross-field DTO rules |
| Validation | `CustomerValidationService` | **Always** | Business rules, domain preconditions, uniqueness |
| Direct mapping | `CustomerMapper` (MapStruct) | Default | Field-to-field DTO ↔ entity ↔ response — inject directly |
| Custom mapping | `CustomerMappingService` | Optional | Multi-source, enrichment, conditional transforms only |
| Service | `CustomerServiceImpl` | Yes | Orchestration, transactions, repository, events, `GlobalResponse` |
| Persistence | `CustomerRepository` | Yes | Load/save only |

## Standard Command Flow (direct MapStruct — default)

```
Request DTO
  → validationService.createValidate(request)
  → customerMapper.toEntity(request)          ← MapStruct, direct inject
  → repository.save(entity)
  → customerMapper.toResponse(saved)          ← MapStruct, direct inject
  → MessageHelper.buildSuccessResponseWithData(...)
```

## Standard Command Flow (custom mapping — when needed)

```
Request DTO
  → validationService.createValidate(request)
  → mappingService.toCreateEntity(request)    ← uses MapStruct internally
  → repository.save(entity)
  → mappingService.toEnrichedResponse(saved)
  → MessageHelper.buildSuccessResponseWithData(...)
```

## Standard Update Flow

```
Request DTO + id
  → load entity (or validationService validates id exists)
  → validationService.updateValidate(request, entity)
  → mapper.updateEntity(request, entity)
  → repository.save(entity)
  → mapper.toResponse(saved)
  → GlobalResponse
```

## Service Implementation Pattern

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerValidationService customerValidationService;

    @Override
    @Transactional
    public GlobalResponse<CustomerResponse> createCustomer(CreateCustomerRequest request)
            throws GlobalException {
        customerValidationService.createValidate(request);

        Customer entity = customerMapper.toEntity(request);
        Customer saved = customerRepository.save(entity);

        log.info("Customer created uid={}", saved.getUid());
        return MessageHelper.buildSuccessResponseWithData(
                SuccessCodeEnum._100.getMessage(),
                customerMapper.toResponse(saved)
        );
    }
}
```

## Validation Service Pattern

```java
@Service
@RequiredArgsConstructor
public class CustomerValidationService {

    private final CustomerRepository customerRepository;

    public void createValidate(CreateCustomerRequest request) throws GlobalException {
        // BR-CUS-001: duplicate email
        if (customerRepository.existsByEmail(request.email())) {
            throw new GlobalException("CUS-CUS-CRE-001", HttpStatus.CONFLICT);
        }
        // BR-CUS-002: corridor allowed for customer type
        if (!isCorridorAllowed(request.customerType(), request.corridorCode())) {
            throw new GlobalException("CUS-CUS-CRE-002", HttpStatus.UNPROCESSABLE_CONTENT);
        }
    }

    public void updateValidate(UpdateCustomerRequest request, Customer entity)
            throws GlobalException {
        if (entity.getStatus() == CustomerStatus.CLOSED) {
            throw new GlobalException("CUS-CUS-UPD-001", HttpStatus.CONFLICT);
        }
    }
}
```

## Naming Conventions

| Artifact | Pattern | Example |
|----------|---------|---------|
| Validation service | `{Aggregate}ValidationService` | `CustomerValidationService` |
| Create validation | `createValidate(request)` | `createValidate(CreateCustomerRequest)` |
| Update validation | `updateValidate(request, entity)` | `updateValidate(UpdateCustomerRequest, Customer)` |
| Delete validation | `deleteValidate(entity)` | `deleteValidate(Customer)` |
| Mapper (MapStruct) | `{Aggregate}Mapper` | `CustomerMapper` — direct inject, no wrapper |
| Custom mapping | `{Aggregate}MappingService` | `CustomerMappingService` — only when MapStruct insufficient |

Use **`createValidate`**, **`updateValidate`**, **`deleteValidate`** — not `validateCreate` — to match SR convention.

## Rules

- Service impl **must inject** `XxxValidationService` for commands — never skip validation layer
- **MapStruct `XxxMapper`** — inject directly for standard mapping; primary dependency ([model-mapper-skill.md](model-mapper-skill.md))
- **`XxxMappingService`** — only when custom transforms exceed MapStruct; delegates field mapping to MapStruct inside
- **ModelMapper** — direct inject acceptable for trivial temporary cases; prefer MapStruct once stable
- Validation service methods return **`void`**; throw **`GlobalException`** on failure
- MapStruct / mapping service methods are pure transformation — no validation, no repository `save`
- Service impl does **not** contain `if` business rule blocks — move them to `XxxValidationService`
- Service impl does **not** inline field copy — use MapStruct or mapping service
- Do **not** create `XxxMappingService` when `XxxMapper` handles the mapping directly

## Anti-Patterns

| Do not | Do instead |
|--------|------------|
| Validate business rules inline in `ServiceImpl` | `validationService.createValidate(request)` |
| Map fields manually in service | `customerMapper.toEntity(request)` or `mappingService.toCreateEntity(request)` |
| Wrap MapStruct in unnecessary `MappingService` | Inject `CustomerMapper` directly |
| Create MappingService for simple field mapping | Use MapStruct `@Mapper` interface |
| Call repository from validation for unrelated aggregates | Inject only what validation needs |
| Mix `@ConstraintValidator` business rules with service rules | DTO format in validator; business rules in `ValidationService` |
| Return entity from service to controller | `mapper.toResponse` inside service, wrap in `GlobalResponse` |

## Testing

Unit-test each layer separately:

- `CustomerValidationServiceTest` — mock repository; assert `GlobalException` codes
- `CustomerServiceTest` — mock validation service + mapper + repository; verify call order

```java
@Test
void shouldCreateCustomer_whenValid() {
    when(mapper.toEntity(request)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(saved);

    service.createCustomer(request);

    verify(validationService).createValidate(request);
    verify(mapper).toEntity(request);
    verify(repository).save(entity);
}
```

## Related

- [validation-skill.md](validation-skill.md)
- [model-mapper-skill.md](model-mapper-skill.md)
- [../code-generation/service-generation-skill.md](../code-generation/service-generation-skill.md)
- [../code-generation/validator-generation-skill.md](../code-generation/validator-generation-skill.md)
- [../templates/validation-service-template.md](../templates/validation-service-template.md)
- [../templates/service-template.md](../templates/service-template.md)
