package com.project.smartinsurance.billingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionResultDto {
    private UUID paymentId;
    private String paymentRef;
    private UUID receiptId;
    private String receiptNo;
    private BigDecimal amount;
    private String paymentStatus;
}
