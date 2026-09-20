package com.project.smartinsurance.billingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionRequest {
    private UUID policyId;
    private String policyNumber;
    private UUID customerId;
    private String customerName;
    private String customerEmail;
    private UUID branchId;
    private BigDecimal amount;
    private String paymentMode;
    private LocalDate paymentDate;
    private String referenceNo;
    private String description;
    private String collectedBy;

    @Builder.Default
    private List<AllocationLine> allocations = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AllocationLine {
        private UUID installmentId;
        private UUID invoiceId;
        private BigDecimal amount;
    }
}
