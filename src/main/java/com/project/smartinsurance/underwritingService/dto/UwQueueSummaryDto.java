package com.project.smartinsurance.underwritingService.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UwQueueSummaryDto {
    private long inQueue;
    private long inAssessment;
    private long pendingDecision;
    private long pendingApproval;
    private long approved;
    private long declined;
    private long infoRequested;
}
