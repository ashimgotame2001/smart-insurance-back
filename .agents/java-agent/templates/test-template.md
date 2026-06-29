# Test Template

## Service Unit Test

Mock validation service, mapper, and repository. Verify orchestration order.

```java
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock CustomerRepository customerRepository;
    @Mock CustomerMapper customerMapper;
    @Mock CustomerValidationService customerValidationService;
    @InjectMocks CustomerServiceImpl customerService;

    @Test
    void shouldCreateCustomer_whenRequestValid() throws GlobalException {
        CreateCustomerRequest request = new CreateCustomerRequest("a@b.com", "RETAIL");
        Customer entity = new Customer();
        Customer saved = new Customer();
        saved.setUid(1L);
        CustomerResponse response = new CustomerResponse(1L, "a@b.com");

        when(customerMapper.toEntity(request)).thenReturn(entity);
        when(customerRepository.save(entity)).thenReturn(saved);
        when(customerMapper.toResponse(saved)).thenReturn(response);

        GlobalResponse<CustomerResponse> result = customerService.createCustomer(request);

        verify(customerValidationService).createValidate(request);
        verify(customerMapper).toEntity(request);
        verify(customerRepository).save(entity);
        assertThat(result.getData()).isEqualTo(response);
    }

    @Test
    void shouldNotSave_whenValidationFails() throws GlobalException {
        CreateCustomerRequest request = new CreateCustomerRequest("dup@b.com", "RETAIL");
        doThrow(new GlobalException("CUS-CUS-CRE-001", HttpStatus.CONFLICT))
                .when(customerValidationService).createValidate(request);

        assertThatThrownBy(() -> customerService.createCustomer(request))
                .isInstanceOf(GlobalException.class);

        verify(customerRepository, never()).save(any());
    }
}
```

## Validation Service Unit Test

```java
@ExtendWith(MockitoExtension.class)
class CustomerValidationServiceTest {

    @Mock CustomerRepository customerRepository;
    @InjectMocks CustomerValidationService customerValidationService;

    @Test
    void shouldThrow_whenEmailExists() {
        CreateCustomerRequest request = new CreateCustomerRequest("dup@b.com", "RETAIL");
        when(customerRepository.existsByEmail("dup@b.com")).thenReturn(true);

        assertThatThrownBy(() -> customerValidationService.createValidate(request))
                .isInstanceOf(GlobalException.class)
                .extracting(ex -> ((GlobalException) ex).getFinalCode())
                .isEqualTo("CUS-CUS-CRE-001");
    }
}
```

Rules: unit test service orchestration and validation service rules separately; MockMvc for controller; name tests with behavior + condition.