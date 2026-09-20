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
public class PremiumRefundDto {
    private UUID id;
    private String refundNo;
    private UUID policyId;
    private String policyNumber;
    private String customerName;
    private BigDecimal amount;
    private String reasonCode;
    private String reason;
    private LocalDate refundDate;
    private String refundStatus;
    private String approvedBy;
}
