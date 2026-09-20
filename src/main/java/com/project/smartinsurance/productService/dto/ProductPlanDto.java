package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPlanDto {
    private UUID id;
    private UUID productId;
    private String code;
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
    private String status;
    private LocalDateTime createdAt;
    private List<ProductCoverageDto> coverages;
}
