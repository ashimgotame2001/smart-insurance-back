package com.project.smartinsurance.billingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingDeskSummaryDto {
    private long dueTodayCount;
    private BigDecimal dueTodayAmount;
    private long overdueCount;
    private BigDecimal outstandingAmount;
    private BigDecimal collectedMtd;
    private long invoicesPending;
    private long receiptsMtd;
}
