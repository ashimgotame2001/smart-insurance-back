package com.project.smartinsurance.underwritingService.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UwApprovalStepDto {
    private UUID id;
    private UUID caseId;
    private UUID decisionId;
    private Integer stepOrder;
    private String stepName;
    private String approverRole;
    private String assignedTo;
    private String stepStatus;
    private String actedBy;
    private LocalDateTime actedAt;
    private String comments;
}
