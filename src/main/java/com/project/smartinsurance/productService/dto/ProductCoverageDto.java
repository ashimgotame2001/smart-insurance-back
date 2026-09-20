package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCoverageDto {
    private UUID id;
    private UUID planId;
    private String code;
    private String name;
    private String description;
    private String coverageType;
    private BigDecimal sumAssuredAmount;
    private BigDecimal premiumRate;
    private BigDecimal premiumAmount;
    private BigDecimal benefitAmount;
    private Integer waitingPeriod;
    private BigDecimal deductible;
    private BigDecimal coinsurance;
    private Integer benefitPeriod;
    private BigDecimal maxBenefit;
    private String coverageTerms;
    private String status;
    private LocalDateTime createdAt;
}
