package com.project.smartinsurance.underwritingService.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnderwritingCaseDto {
    private UUID id;
    private String caseNumber;
    private UUID policyId;
    private String policyNumber;
    private UUID customerId;
    private String customerName;
    private String productCode;
    private String productName;
    private String lineOfBusiness;
    private String caseStatus;
    private String priority;
    private String assignee;
    private String referralReason;
    private String referralRules;
    private String submittedBy;
    private LocalDateTime submittedAt;
    private LocalDateTime dueAt;
    private LocalDateTime closedAt;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UnderwritingDecisionDto latestDecision;
    private List<UwApprovalStepDto> approvalSteps;
    private List<UwAssessmentDto> assessments;
}
