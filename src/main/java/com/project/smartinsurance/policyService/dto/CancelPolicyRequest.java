package com.project.smartinsurance.policyService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelPolicyRequest {

    @NotBlank
    private String reason;

    @NotNull
    private LocalDate effectiveDate;
}
