package com.project.smartinsurance.productService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EligibilityRuleCreateRequest {
    @NotNull
    private UUID productId;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String conditionExpression;

    @NotBlank
    private String actionOnFail;
}
