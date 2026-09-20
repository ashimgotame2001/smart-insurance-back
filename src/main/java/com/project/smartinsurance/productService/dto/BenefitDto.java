package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BenefitDto {
    private UUID id;
    private UUID productId;
    private String productCode;
    private String productName;
    private String code;
    private String name;
    private String description;
    private String benefitType;
    private BigDecimal benefitAmount;
    private BigDecimal maxBenefit;
    private Integer waitingPeriod;
    private Integer benefitPeriod;
    private Boolean renewable;
    private String benefitTerms;
    private String status;
    private LocalDateTime createdAt;
}