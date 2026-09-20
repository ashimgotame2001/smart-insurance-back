package com.project.smartinsurance.billingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumAdjustmentDto {
    private UUID id;
    private String adjustmentNo;
    private UUID policyId;
    private String policyNumber;
    private UUID installmentId;
    private String customerName;
    private String adjustmentType;
    private BigDecimal amount;
    private String reason;
    private LocalDate adjustmentDate;
    private String adjustmentStatus;
}
