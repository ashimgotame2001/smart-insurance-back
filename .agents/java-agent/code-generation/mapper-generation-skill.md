# Mapper Generation Skill

## Primary: MapStruct

Generate a MapStruct interface for every aggregate with REST APIs. This is the **default** mapping approach — inject directly into `ServiceImpl` without a separate mapping service.

## Rules

- `@Mapper(componentModel = "spring")`
- Separate methods: `toEntity`, `toResponse`, `toSummary`, `updateEntity`
- Use `@Mapping`, `@MappingTarget`, `@AfterMapping` for custom field logic **within MapStruct** before creating a mapping service
- Ignore audit and internal fields on response mapping
- Map collections and `Page` types via `default` methods
- Package: `mapper/`

## When to Also Generate `XxxMappingService`

Generate [mapping-service-template.md](../templates/mapping-service-template.md) only when:

- Multiple source objects feed one response
- Enrichment requires read-only repository lookups
- Conditional mapping logic exceeds MapStruct `@AfterMapping`

Otherwise, MapStruct alone — no mapping service.

## ModelMapper (fallback)

Do not generate ModelMapper configuration by default. Use Platform `ModelMapper` bean only for temporary/simple cases per [model-mapper-skill.md](../programming/model-mapper-skill.md).

## Gradle (per module)

```gradle
implementation 'org.mapstruct:mapstruct'
annotationProcessor 'org.mapstruct:mapstruct-processor'
```

## Templates

- Direct mapping: [../templates/mapper-template.md](../templates/mapper-template.md)
- Custom mapping: [../templates/mapping-service-template.md](../templates/mapping-service-template.md)
- Patterns: [../programming/model-mapper-skill.md](../programming/model-mapper-skill.md)
