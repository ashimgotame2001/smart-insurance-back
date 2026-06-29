# Mapper Template

MapStruct is the **primary** mapping dependency. Inject this interface **directly** into `ServiceImpl` — no wrapper mapping service for standard field mapping.

```java
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toResponse(Customer entity);

    Customer toEntity(CreateCustomerRequest request);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(UpdateCustomerRequest request, @MappingTarget Customer entity);

    CustomerSummary toSummary(CustomerSummaryProjection projection);

    List<CustomerSummary> toSummaryList(List<CustomerSummaryProjection> projections);

    default CustomerPageResponse toPageResponseFromProjection(Page<CustomerSummaryProjection> page) {
        return new CustomerPageResponse(
            toSummaryList(page.getContent()),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
        );
    }
}
```

## Gradle

```gradle
implementation 'org.mapstruct:mapstruct'
annotationProcessor 'org.mapstruct:mapstruct-processor'
```

## Rules

- MapStruct `@Mapper(componentModel = "spring")` — primary approach
- Ignore audit/internal fields on response and update mappings
- Use `@AfterMapping` / `default` methods for logic that stays within MapStruct
- If logic exceeds MapStruct, create [mapping-service-template.md](mapping-service-template.md) instead of bloating the mapper
- Do not wrap this interface in an unnecessary `XxxMappingService`

See [../programming/model-mapper-skill.md](../programming/model-mapper-skill.md).
