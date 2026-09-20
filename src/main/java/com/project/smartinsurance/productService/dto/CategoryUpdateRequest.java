package com.project.smartinsurance.productService.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateRequest {
    private String name;
    private String description;
    private String parentCode;
    private Integer sortOrder;
}