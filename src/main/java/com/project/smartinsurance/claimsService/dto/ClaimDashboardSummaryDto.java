package com.project.smartinsurance.claimsService.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimDashboardSummaryDto {
    private long openCount;
    private long pendingApprovalCount;
    private BigDecimal paidYtdAmount;
    private Double avgSettlementDays;
    private long fraudOpenCount;
    private long aging0to7;
    private long aging8to30;
    private long aging31plus;
    private Map<String, Long> agingBuckets;
}
