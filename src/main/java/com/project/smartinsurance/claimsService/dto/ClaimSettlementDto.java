package com.project.smartinsurance.claimsService.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimSettlementDto {
    private UUID id;
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
    private LocalDateTime settledAt;
    private String settledBy;
}
