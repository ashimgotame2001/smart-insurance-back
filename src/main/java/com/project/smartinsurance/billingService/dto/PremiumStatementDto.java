package com.project.smartinsurance.billingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.project.smartinsurance.policyService.dto.PolicyPremiumDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumStatementDto {
    private UUID policyId;
    private String policyNumber;
    private String customerName;
    private BigDecimal totalPayable;
    private BigDecimal totalPaid;
    private BigDecimal totalOutstanding;
    @Builder.Default
    private List<PolicyPremiumDto> installments = new ArrayList<>();
    @Builder.Default
    private List<BillingPaymentDto> payments = new ArrayList<>();
}
