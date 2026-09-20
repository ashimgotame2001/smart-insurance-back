package com.project.smartinsurance.claimsService.dto;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimSettlementRequest {
    private BigDecimal grossAmount;
    private BigDecimal deductibleAmount;
    private BigDecimal depreciationAmount;
    private BigDecimal bettermentAmount;
    private BigDecimal unpaidPremiumOffset;
    private BigDecimal recoveryReserve;
    private BigDecimal taxAmount;
    private BigDecimal netAmount;
    private String paymentMethod;
    private String remarks;
    private String settledBy;
}
