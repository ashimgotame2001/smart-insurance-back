package com.project.smartinsurance.billingService.dto;

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
public class BillingReconcileDto {
    private BigDecimal billingCollectedMtd;
    private BigDecimal receiptsIssuedMtd;
    private BigDecimal gatewayPending;
    private long unmatchedOnlineCount;
    @Builder.Default
    private List<String> notes = new ArrayList<>();
}
