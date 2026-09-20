package com.project.smartinsurance.policyService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyPremiumDto {
    private UUID id;
    private UUID policyId;
    private String policyNumber;
    private String customerName;
    private BigDecimal basePremium;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal surchargeAmount;
    private BigDecimal feeAmount;
    private BigDecimal totalPremium;
    private BigDecimal payableAmount;
    private BigDecimal paidAmount;
    private BigDecimal balanceAmount;
    private Integer installmentNumber;
    private LocalDate dueDate;
    private LocalDate paidDate;
    private String paymentStatus;
    private UUID invoiceId;
    private Integer daysOverdue;
}
