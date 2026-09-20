package com.project.smartinsurance.productService.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EligibilityRuleUpdateRequest {
    private String name;
    private String description;
    private String conditionExpression;
    private String actionOnFail;
}
