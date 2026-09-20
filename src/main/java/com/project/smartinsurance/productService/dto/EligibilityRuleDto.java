package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EligibilityRuleDto {
    private UUID id;
    private UUID productId;
    private String name;
    private String description;
    private String conditionExpression;
    private String actionOnFail;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
