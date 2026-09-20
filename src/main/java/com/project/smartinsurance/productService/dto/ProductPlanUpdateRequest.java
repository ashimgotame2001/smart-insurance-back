package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPlanUpdateRequest {
    private String name;
    private String description;
    private Integer minEntryAge;
    private Integer maxEntryAge;
    private BigDecimal minSumAssured;
    private BigDecimal maxSumAssured;
    private Integer minTerm;
    private Integer maxTerm;
    private String premiumType;
    private String premiumFrequency;
    private Integer premiumPayingTerm;
}
