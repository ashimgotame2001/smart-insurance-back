package com.project.smartinsurance.productService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCreateRequest {
    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;
    private String parentCode;
    private Integer sortOrder;
}