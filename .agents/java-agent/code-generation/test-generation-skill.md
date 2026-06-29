# Test Generation Skill

## Minimum Tests

| Layer | Test type | Focus |
|-------|-----------|-------|
| Service | Unit (`@ExtendWith(MockitoExtension.class)`) | Orchestration order; mock validation + mapper |
| Validation service | Unit | Business rules, `GlobalException` codes |
| Controller | Slice / MockMvc | HTTP status, validation errors |
| Repository | `@DataJpaTest` | Custom queries (when complex) |

## Naming

`should{ExpectedBehavior}_when{Condition}`

## Rules

- Mock **`XxxValidationService`**, mapper, and repository in service tests
- Unit-test **`XxxValidationService`** separately for business rules
- Verify call order: `createValidate` → `toEntity` → `save` on happy path
- Cover happy path + primary error cases per requirement
- Tag or name tests with requirement ID in display name when useful

## Template

See [../templates/test-template.md](../templates/test-template.md).

## Integration Tests

For `@WebMvcTest`, `@DataJpaTest`, `@SpringBootTest`, and messaging tests, see [integration-test-generation-skill.md](integration-test-generation-skill.md).
