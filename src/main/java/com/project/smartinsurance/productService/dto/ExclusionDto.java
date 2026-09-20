package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExclusionDto {
    private UUID id;
    private UUID productId;
    private String productCode;
    private String productName;
    private String code;
    private String name;
    private String description;
    private String exclusionType;
    private String coverageCode;
    private String reason;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private String status;
    private LocalDateTime createdAt;
}