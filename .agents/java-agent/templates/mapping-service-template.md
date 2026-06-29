# Mapping Service Template

Optional custom mapping layer — use **only** when MapStruct or ModelMapper direct inject cannot handle the transformation. For standard field mapping, use [mapper-template.md](mapper-template.md) and inject `XxxMapper` directly into `ServiceImpl`.

## Class (`mapper/CustomerMappingService.java`)

```java
package com.edx.platform.sr.customer.mapper;

import com.edx.platform.sr.customer.domain.entity.Customer;
import com.edx.platform.sr.customer.domain.repository.BeneficiaryRepository;
import com.edx.platform.sr.customer.model.request.CreateCustomerRequest;
import com.edx.platform.sr.customer.model.response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerMappingService {

    private final CustomerMapper customerMapper;
    private final BeneficiaryRepository beneficiaryRepository;

    /**
     * Custom create mapping — e.g. set defaults MapStruct cannot derive from request alone.
     */
    public Customer toCreateEntity(CreateCustomerRequest request) {
        Customer entity = customerMapper.toEntity(request);
        entity.setStatus(CustomerStatus.PENDING);
        entity.setReferenceCode(generateReference());
        return entity;
    }

    /**
     * Enriched response — multiple sources beyond direct field mapping.
     */
    public CustomerResponse toEnrichedResponse(Customer entity) {
        CustomerResponse base = customerMapper.toResponse(entity);
        int count = beneficiaryRepository.countByCustomerUid(entity.getUid());
        return base.withBeneficiaryCount(count);
    }

    private String generateReference() {
        return "CUS-" + System.currentTimeMillis();
    }
}
```

## Service impl — custom mapping

```java
Customer entity = customerMappingService.toCreateEntity(request);
Customer saved = customerRepository.save(entity);
return MessageHelper.buildSuccessResponseWithData(
        SuccessCodeEnum._100.getMessage(),
        customerMappingService.toEnrichedResponse(saved)
);
```

## Service impl — direct MapStruct (preferred, no mapping service)

```java
Customer entity = customerMapper.toEntity(request);
Customer saved = customerRepository.save(entity);
return MessageHelper.buildSuccessResponseWithData(
        SuccessCodeEnum._100.getMessage(),
        customerMapper.toResponse(saved)
);
```

## Rules

- Delegate field-to-field mapping to **`CustomerMapper`** (MapStruct) inside this service
- Create this class only when custom logic is required — do not wrap simple MapStruct calls
- No validation (`XxxValidationService`) or persistence (`repository.save`) in mapping service
- May inject repositories **for read-only enrichment** only

## Related

- [mapper-template.md](mapper-template.md)
- [../programming/model-mapper-skill.md](../programming/model-mapper-skill.md)
