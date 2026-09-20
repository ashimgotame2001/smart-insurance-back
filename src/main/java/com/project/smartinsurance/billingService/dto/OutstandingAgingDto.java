package com.project.smartinsurance.billingService.dto;

import com.project.smartinsurance.policyService.dto.PolicyPremiumDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutstandingAgingDto {
    private BigDecimal current;
    private BigDecimal days1to30;
    private BigDecimal days31to60;
    private BigDecimal days61to90;
    private BigDecimal days90plus;
    private BigDecimal total;
    @Builder.Default
    private List<PolicyPremiumDto> rows = new ArrayList<>();
}
