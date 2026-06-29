# Projection Template

## Interface projection

```java
package com.edx.platform.sr.remittance.domain.projection;

import java.math.BigDecimal;
import java.time.Instant;

public interface RemittanceSummaryProjection {

    Long getUid();

    Long getCustomerId();

    String getStatus();

    BigDecimal getAmount();

    String getCurrency();

    Instant getCreatedAt();
}
```

## Repository

```java
public interface RemittanceTransactionRepository
        extends JpaRepository<RemittanceTransaction, Long> {

    Page<RemittanceSummaryProjection> findByStatus(String status, Pageable pageable);

    @Query("""
        select r.uid as uid, r.customerId as customerId, r.status as status,
               r.amount as amount, r.currency as currency, r.createdAt as createdAt
        from RemittanceTransaction r
        where r.customerId = :customerId
        """)
    Page<RemittanceSummaryProjection> findSummariesByCustomerId(Long customerId, Pageable pageable);
}
```

## Service mapping

```java
@Transactional(readOnly = true)
public RemittancePageResponse list(int page, int size, String status) {
    Pageable pageable = PageRequest.of(page, size);
    Page<RemittanceSummaryProjection> result = repository.findByStatus(status, pageable);
    return mapper.toPageResponseFromProjection(result);
}
```

Rules: read-only; scalar fields only; align with OpenAPI `RemittanceSummary`.
