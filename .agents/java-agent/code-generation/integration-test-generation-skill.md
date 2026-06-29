# Integration Test Generation Skill

## Purpose

Generate integration tests that verify Spring context wiring, HTTP contracts, persistence, and messaging boundaries — complementing unit tests from [test-generation-skill.md](test-generation-skill.md).

## When to Use

- New or changed REST API — verify full request/response against OpenAPI
- Custom JPA queries or projections — verify against real SQL semantics
- gRPC client adapters — verify stub interaction or test container against provider
- Kafka/event handlers — verify publish/consume with embedded or Testcontainers broker
- Cross-layer flows (controller → service → repository)

## Test Pyramid (SR modules)

| Layer | Annotation | Scope |
|-------|------------|-------|
| Unit | `@ExtendWith(MockitoExtension.class)` | Service, validator, adapter mapping |
| Slice — web | `@WebMvcTest` + `@MockBean` services | Controller contract, validation, status codes |
| Slice — JPA | `@DataJpaTest` | Repositories, projections, `@Query` |
| Integration | `@SpringBootTest` + `@AutoConfigureMockMvc` | Full HTTP stack with mocked externals |
| Integration — DB | `@SpringBootTest` + Testcontainers PostgreSQL | Migrations, transactions, constraints |
| Integration — messaging | `@SpringBootTest` + Testcontainers Kafka | Event publish/consume, idempotency |

Prefer the **narrowest** test type that proves the behavior. Do not boot the full application for logic already covered by unit tests.

## Naming and Location

```
src/test/java/com/edx/platform/sr/<module>/
├── application/ExampleServiceTest.java           ← unit
├── api/ExampleControllerTest.java                ← @WebMvcTest
├── domain/repository/ExampleRepositoryTest.java  ← @DataJpaTest
└── integration/ExampleApiIntegrationTest.java  ← full stack
```

Pattern: `should{ExpectedBehavior}_when{Condition}`

Tag display names with requirement ID when useful: `@DisplayName("REM-SEND-001 should create remittance when request valid")`

## Controller Slice (`@WebMvcTest`)

```java
@WebMvcTest(RemittanceController.class)
@Import(SecurityTestConfiguration.class)  // Platform test helper if available
class RemittanceControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean RemittanceService remittanceService;

    @Test
    void shouldReturn201_whenCreateValid() throws Exception {
        when(remittanceService.createRemittance(any()))
            .thenReturn(MessageHelper.buildSuccessResponseWithData(new RemittanceResponse(/* ... */)));

        mockMvc.perform(post("/api/v1/remittances")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"senderId":1,"beneficiaryId":2,"amount":100.00,"currency":"USD"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.data.uid").exists());
    }

    @Test
    void shouldReturn400_whenValidationFails() throws Exception {
        mockMvc.perform(post("/api/v1/remittances")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("FAILED"));
    }
}
```

Assert **`GlobalResponse`** shape (`status`, `code`, `message`, `data`) per [request-response-schema-skill.md](../openapi/request-response-schema-skill.md).

## Repository Slice (`@DataJpaTest`)

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class RemittanceRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18");

    @Autowired RemittanceTransactionRepository repository;

    @Test
    void shouldFindByStatus_whenProjectionQuery() {
        // seed entity, assert projection fields only
    }
}
```

Use Testcontainers when SQL dialect or constraint behavior matters; use in-memory H2 only if the repo already standardizes on it for slices.

## Full Stack Integration (`@SpringBootTest`)

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class RemittanceApiIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired MockMvc mockMvc;
    @MockBean OutboundPaymentPort paymentPort;  // external systems always mocked

    @Test
    void shouldPersistAndReturnRemittance_whenEndToEndCreate() throws Exception {
        when(paymentPort.route(any())).thenReturn(new PayoutResult(/* ... */));

        mockMvc.perform(post("/api/v1/remittances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreateJson()))
            .andExpect(status().isCreated());

        // optional: verify DB state via repository
    }
}
```

## Event Integration

```java
@SpringBootTest
@Testcontainers
class RemittanceSubmittedEventIntegrationTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.0"));

    @Autowired RemittanceEventPublisher publisher;
    @Autowired RemittanceSubmittedHandler handler;  // or await listener

    @Test
    void shouldProcessEventIdempotently_whenDuplicateDelivery() {
        // publish same event twice, assert single side effect
    }
}
```

## gRPC Adapter Integration

- **Unit level**: mock `ForexQuoteServiceGrpc.ForexQuoteServiceBlockingStub` and assert `GlobalException` mapping ([grpc-client-adapter-skill.md](../programming/grpc-client-adapter-skill.md))
- **Integration level**: use provider test stub or `@GrpcMock` only when the repo already supports it — do not invent a custom gRPC test harness

## Rules

- Mock **all outbound** ports (payment, gRPC, partner HTTP) in integration tests — only infrastructure under test uses real beans
- Use `@Transactional` rollback on DB tests when tests only need persistence verification and the repo pattern supports it
- Do not assert on log output unless testing observability requirements explicitly
- One primary scenario per requirement ID at integration level; edge cases stay in unit tests
- Test security with Platform test utilities or `@WithMockUser` — never disable security globally without repo convention
- Clean up Testcontainers in CI-friendly way (static containers per class)

## Minimum Coverage for New Features

| Feature element | Required integration test |
|-----------------|---------------------------|
| New REST endpoint | `@WebMvcTest` minimum; `@SpringBootTest` when service + DB path is non-trivial |
| New repository query | `@DataJpaTest` when query is custom |
| New event consumer | Kafka integration or handler unit test with embedded broker |
| New gRPC adapter | Unit test for status mapping; optional container test |

## Output

Deliver test classes with:

1. Clear arrange/act/assert structure
2. Requirement ID in display name or JavaDoc where applicable
3. Test data builders or fixture methods for readable requests
4. No hardcoded secrets; use `@DynamicPropertySource` or `application-test.yml`

## Related

- [test-generation-skill.md](test-generation-skill.md)
- [../templates/test-template.md](../templates/test-template.md)
- [../programming/grpc-client-adapter-skill.md](../programming/grpc-client-adapter-skill.md)
- [../programming/event-driven-skill.md](../programming/event-driven-skill.md)
