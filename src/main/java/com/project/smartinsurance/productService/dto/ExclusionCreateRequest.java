package com.project.smartinsurance.productService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExclusionCreateRequest {
    @NotNull
    private UUID productId;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;
    private String exclusionType;
    private String coverageCode;
    private String reason;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}