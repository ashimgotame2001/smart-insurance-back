package com.project.smartinsurance.billingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingPaymentDto {
    private UUID id;
    private String paymentRef;
    private UUID policyId;
    private String policyNumber;
    private UUID customerId;
    private String customerName;
    private UUID branchId;
    private BigDecimal amount;
    private String paymentMode;
    private LocalDate paymentDate;
    private LocalDate valueDate;
    private String referenceNo;
    private String description;
    private String collectedBy;
    private UUID receiptId;
    private String receiptNo;
    private String paymentStatus;
}
