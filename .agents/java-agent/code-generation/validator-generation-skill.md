# Validator Generation Skill

## Two Validation Layers

| Layer | Class | Purpose |
|-------|-------|---------|
| DTO validation | `@Constraint` + `ConstraintValidator` | Field format, required fields, cross-field DTO rules at API boundary |
| Business validation | `XxxValidationService` | Uniqueness, state guards, domain rules — injected into service impl |

Generate **both** when creating a new aggregate feature.

## DTO Validator (`@Constraint`)

- Class-level `@Constraint` for cross-field validation
- Implement `ConstraintValidator<Annotation, DTO>`
- Inject repositories only when needed for simple DTO-level checks
- Return clear default messages aligned with Figma copy

### Template

See [../templates/validator-template.md](../templates/validator-template.md).

## Validation Service (`XxxValidationService`)

- One service per aggregate: `CustomerValidationService`, `RemittanceValidationService`
- Package: `validator/`
- Methods: **`createValidate(request)`**, **`updateValidate(request, entity)`**, **`deleteValidate(entity)`**
- Return `void`; throw `GlobalException` with message codes
- Inject repositories for business-rule checks (uniqueness, state, related aggregates)

### Template

See [../templates/validation-service-template.md](../templates/validation-service-template.md).

## Service Impl Usage

```java
customerValidationService.createValidate(request);
Customer entity = customerMapper.toEntity(request);
Customer saved = customerRepository.save(entity);
```

Validation service is called **before** mapping to entity on create operations.

## Related

- [../programming/validation-skill.md](../programming/validation-skill.md)
- [../programming/service-layer-separation-skill.md](../programming/service-layer-separation-skill.md)
- [service-generation-skill.md](service-generation-skill.md)
