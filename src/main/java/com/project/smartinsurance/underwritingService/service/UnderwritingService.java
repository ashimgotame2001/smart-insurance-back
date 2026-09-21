package com.project.smartinsurance.underwritingService.service;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.underwritingService.dto.*;

import java.util.List;
import java.util.UUID;

public interface UnderwritingService {
    UnderwritingCaseDto submitForUnderwriting(SubmitForUnderwritingRequest request);
    UnderwritingCaseDto getCase(UUID id);
    UnderwritingCaseDto getCaseByPolicy(UUID policyId);
    PagedData<UnderwritingCaseDto> searchCases(String status, String assignee, String lob, String search, int page, int size);
    UwQueueSummaryDto queueSummary();
    UnderwritingCaseDto assign(UUID id, UwAssignRequest request);
    UnderwritingDecisionDto recordDecision(UUID caseId, UwDecisionRequest request);
    List<UnderwritingDecisionDto> listDecisions(UUID caseId);
    UwApprovalStepDto approveStep(UUID caseId, UUID stepId, UwApprovalActionRequest request);
    UwApprovalStepDto rejectStep(UUID caseId, UUID stepId, UwApprovalActionRequest request);
    List<UwApprovalStepDto> listApprovals(UUID caseId);
    UwAssessmentDto createAssessment(UUID caseId, UwAssessmentRequest request);
    UwAssessmentDto updateAssessment(UUID assessmentId, UwAssessmentRequest request);
    List<UwAssessmentDto> listAssessments(UUID caseId, String type);
    PagedData<UwAssessmentDto> listAssessmentsByType(String type, int page, int size);
    UwAssessmentDto computeRiskScore(UUID caseId);
    boolean isUnderwritingCleared(UUID policyId);
    boolean requiresUnderwriting(String productCode);
}
