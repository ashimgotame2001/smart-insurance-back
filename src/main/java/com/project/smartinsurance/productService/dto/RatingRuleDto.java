package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingRuleDto {
    private UUID id;
    private UUID productId;
    private String name;
    private String description;
    private String ruleType;
    private String factorName;
    private String formula;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private BigDecimal defaultValue;
    private Integer priority;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
