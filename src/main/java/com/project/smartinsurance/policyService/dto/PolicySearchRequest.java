package com.project.smartinsurance.policyService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicySearchRequest {
    private String policyNumber;
    private String customerName;
    private UUID customerId;
    private String policyStatus;
    private String productCode;
    private UUID branchId;
    private LocalDate effectiveDateFrom;
    private LocalDate effectiveDateTo;
    private LocalDate expiryDateFrom;
    private LocalDate expiryDateTo;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDir = "desc";
}
