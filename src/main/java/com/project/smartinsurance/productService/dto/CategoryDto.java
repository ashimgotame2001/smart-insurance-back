package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private String parentCode;
    private Integer sortOrder;
    private String status;
    private LocalDateTime createdAt;
}