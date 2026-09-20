package com.project.smartinsurance.policyService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoverageDto {
    private UUID id;
    private UUID policyId;
    private String coverageCode;
    private String coverageName;
    private BigDecimal sumInsured;
    private BigDecimal deductible;
    private BigDecimal limitAmount;
    private BigDecimal premiumAmount;
    private String status;
    private Integer waitingPeriod;
}
