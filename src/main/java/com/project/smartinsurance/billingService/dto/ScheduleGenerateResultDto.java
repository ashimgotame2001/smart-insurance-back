package com.project.smartinsurance.billingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleGenerateResultDto {
    private UUID policyId;
    private String policyNumber;
    private int installmentCount;
    private BigDecimal totalPayable;
    private String frequency;
    private String message;
}
