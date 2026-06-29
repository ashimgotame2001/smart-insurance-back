# Validation Skill

## Purpose

Apply validation at three boundaries: API DTO, business validation service, and optional custom Jakarta validators.

## Layers

| Layer | Class / mechanism | Responsibility |
|-------|-------------------|----------------|
| DTO | `@NotNull`, `@Size`, `@Pattern`, `@Valid`, `@Constraint` | Field format, required fields, cross-field DTO rules |
| Business | `XxxValidationService` | Uniqueness, state guards, domain preconditions — **injected into service impl** |
| Service impl | Orchestration only | Calls `validationService.createValidate(request)` before map/save |

## Controller

```java
public ResponseEntity<GlobalResponse<CustomerResponse>> createCustomer(
        @Valid @RequestBody CreateCustomerRequest request) throws GlobalException {
    return ResponseEntity.ok(customerService.createCustomer(request));
}
```

Jakarta `@Valid` runs at the API boundary. Business rules run in `CustomerValidationService`.

## Validation Service Pattern

```java
@Service
@RequiredArgsConstructor
public class CustomerValidationService {

    private final CustomerRepository customerRepository;

    public void createValidate(CreateCustomerRequest request) throws GlobalException {
        if (customerRepository.existsByEmail(request.email())) {
            throw new GlobalException("CUS-CUS-CRE-001", HttpStatus.CONFLICT);
        }
    }
}
```

Service impl calls validation **before** mapping:

```java
customerValidationService.createValidate(request);
Customer entity = customerMapper.toEntity(request);
Customer saved = customerRepository.save(entity);
```

## Custom Validator Pattern (DTO)

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BeneficiaryAccountValidator.class)
public @interface ValidBeneficiaryAccount { }
```

Use for DTO-level cross-field checks. Move business rules that need repository/state logic to `XxxValidationService`.

## Messages

- Use message codes in validation range `200–299`: `AAA-BBB-OPR-2NN` — see [message-code-convention-skill.md](message-code-convention-skill.md)
- Use message keys for i18n: `{validation.RMS-RMS-CRE-201}`
- Align validation messages with Figma error text where specified

## Rules

- Validate input format at API boundary (`@Valid`)
- Business validation in **`XxxValidationService`** — not inline in `ServiceImpl`
- Command flow: **`createValidate` → `mapper.toEntity` → `repository.save`**
- Do not rely on database constraints alone for user-facing errors

See [../code-generation/validator-generation-skill.md](../code-generation/validator-generation-skill.md), [service-layer-separation-skill.md](service-layer-separation-skill.md), and [message-code-convention-skill.md](message-code-convention-skill.md).
