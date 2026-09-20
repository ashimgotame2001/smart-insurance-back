package com.project.smartinsurance.policyService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndorsementRequest {

    @NotNull
    private String endorsementType;

    @NotBlank
    private String changeDescription;

    private BigDecimal premiumDifference;

    @NotNull
    private LocalDate effectiveDate;

    private String requestedBy;
}
