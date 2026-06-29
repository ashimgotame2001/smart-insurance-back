# Model Mapper Skill

## Purpose

Map between entities, projections, and API models without exposing the persistence layer.

**MapStruct is the primary mapping dependency.** Inject the generated MapStruct mapper directly into `XxxServiceImpl` for direct field mapping. Add a separate **`XxxMappingService`** only when custom transformation logic is required.

## Mapping Options (priority order)

| Approach | When to use | Injected into ServiceImpl |
|----------|-------------|---------------------------|
| **MapStruct** (`XxxMapper`) | Default — direct field mapping, projections, page wrappers | Yes — **no extra mapping layer** |
| **ModelMapper** | Simple ad-hoc mapping where MapStruct is not yet defined; legacy one-offs | Yes — inject Platform `ModelMapper` bean directly |
| **Mapping service** (`XxxMappingService`) | Custom logic: multi-source, enrichment, conditional transforms | Yes — only when MapStruct/ModelMapper are insufficient |

## Primary: MapStruct (direct inject)

Use for all standard CRUD and list APIs. The MapStruct interface **is** the mapper — do not wrap it in another service.

```java
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toResponse(Customer entity);

    Customer toEntity(CreateCustomerRequest request);

    void updateEntity(UpdateCustomerRequest request, @MappingTarget Customer entity);

    CustomerSummary toSummary(CustomerSummaryProjection projection);

    default CustomerPageResponse toPageResponseFromProjection(Page<CustomerSummaryProjection> page) {
        return new CustomerPageResponse(
            page.getContent().stream().map(this::toSummary).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
        );
    }
}
```

Service impl uses MapStruct **directly**:

```java
customerValidationService.createValidate(request);
Customer entity = customerMapper.toEntity(request);
Customer saved = customerRepository.save(entity);
return MessageHelper.buildSuccessResponseWithData(
        SuccessCodeEnum._100.getMessage(),
        customerMapper.toResponse(saved)
);
```

### Gradle dependency

```gradle
implementation 'org.mapstruct:mapstruct'
annotationProcessor 'org.mapstruct:mapstruct-processor'
```

## Secondary: ModelMapper (direct inject, limited use)

Use only when:

- Mapping is trivial and a MapStruct interface does not yet exist
- Prototyping before committing to generated mappers

```java
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final ModelMapper modelMapper;

    Customer entity = modelMapper.map(request, Customer.class);
}
```

Prefer adding a MapStruct interface once the mapping stabilizes. Do **not** use ModelMapper as the long-term default.

### Gradle dependency (when needed)

```gradle
implementation 'org.modelmapper:modelmapper'
```

## Custom mapping layer (`XxxMappingService`)

Create **`CustomerMappingService`** only when mapping requires logic beyond MapStruct annotations:

| Trigger | Example |
|---------|---------|
| Multiple source objects | Merge `Customer` + `KycProfile` into one response |
| Enrichment from other services | Resolve display names from related aggregates |
| Conditional field mapping | Map different shapes based on customer type |
| Partner/external model conversion | Complex adapter transforms before/after MapStruct |

```java
@Service
@RequiredArgsConstructor
public class CustomerMappingService {

    private final CustomerMapper customerMapper;  // delegate direct mapping to MapStruct
    private final BeneficiaryRepository beneficiaryRepository;

    public CustomerResponse toEnrichedResponse(Customer entity) {
        CustomerResponse base = customerMapper.toResponse(entity);
        int beneficiaryCount = beneficiaryRepository.countByCustomerUid(entity.getUid());
        return base.withBeneficiaryCount(beneficiaryCount);
    }
}
```

Service impl injects **`CustomerMappingService`** instead of calling MapStruct directly for that operation only. Other methods on the same aggregate may still inject `CustomerMapper` directly.

### Rules for mapping service

- Delegate field-to-field work to MapStruct inside the mapping service — do not duplicate MapStruct logic
- No validation — validation stays in `XxxValidationService`
- No `repository.save` — persistence stays in service impl
- One `{Aggregate}MappingService` per aggregate when custom mapping is needed; omit entirely when MapStruct alone suffices

## Decision Flow

```
Can MapStruct map fields directly (including @AfterMapping / default methods)?
  YES → inject XxxMapper in ServiceImpl — no mapping service
  NO → does logic need multiple sources, enrichment, or conditional transforms?
    YES → create XxxMappingService; may use XxxMapper internally
    NO → use ModelMapper directly in ServiceImpl (temporary) or add MapStruct @AfterMapping
```

## Package Layout

```
mapper/
├── CustomerMapper.java           # MapStruct — default, always generate
└── CustomerMappingService.java   # optional — custom mapping only
```

## Rules

- **MapStruct first** — generate `XxxMapper` for every aggregate with REST APIs
- **Direct inject** — MapStruct and ModelMapper go into `ServiceImpl`; no wrapper unless custom logic requires it
- Separate request and response models (`model.request`, `model.response`)
- Never map entity inline in controller or service impl
- Ignore sensitive/internal fields on response mapping (`@Mapping(target = "...", ignore = true)`)
- MapStruct methods are pure transformation — no validation, no `save`
- Do not create `XxxMappingService` when `XxxMapper` handles the case

## Collection and Projection Mapping

- Map `Page<Projection>` to Platform `PageResponse<SummaryModel>` via MapStruct `default` methods ([projection-skill.md](projection-skill.md))
- Map `Page<Entity>` to `PageResponse<ResponseModel>` only for detail-heavy list cases

## Related

- [../code-generation/mapper-generation-skill.md](../code-generation/mapper-generation-skill.md)
- [../code-generation/model-generation-skill.md](../code-generation/model-generation-skill.md)
- [service-layer-separation-skill.md](service-layer-separation-skill.md)
- [../templates/mapper-template.md](../templates/mapper-template.md)
- [../templates/mapping-service-template.md](../templates/mapping-service-template.md)
