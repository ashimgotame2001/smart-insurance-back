package com.project.smartinsurance.claimsService.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimCoverageItemDto {
    private UUID id;
    private String coverageCode;
    private String coverageName;
    private BigDecimal sumInsured;
    private BigDecimal claimedAmount;
    private BigDecimal deductible;
    private BigDecimal coinsurancePercent;
    private BigDecimal approvedAmount;
}
