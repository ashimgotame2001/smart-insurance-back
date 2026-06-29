# Validator Template

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CreateRemittanceRequestValidator.class)
public @interface ValidCreateRemittanceRequest {
    String message() default "{validation.remittance.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

@Component
@RequiredArgsConstructor
public class CreateRemittanceRequestValidator
        implements ConstraintValidator<ValidCreateRemittanceRequest, CreateRemittanceRequest> {

    @Override
    public boolean isValid(CreateRemittanceRequest value, ConstraintValidatorContext context) {
        if (value == null) return true;
        // BR-REM-012: cross-field validation
        return true;
    }
}
```

Apply on request DTO: `@ValidCreateRemittanceRequest` at class level.
