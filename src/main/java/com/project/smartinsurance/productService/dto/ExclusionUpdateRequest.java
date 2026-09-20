package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExclusionUpdateRequest {
    private String name;
    private String description;
    private String exclusionType;
    private String coverageCode;
    private String reason;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}