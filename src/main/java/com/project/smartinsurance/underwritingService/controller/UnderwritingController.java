package com.project.smartinsurance.underwritingService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.underwritingService.dto.*;
import com.project.smartinsurance.underwritingService.service.UnderwritingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/underwriting")
@RequiredArgsConstructor
public class UnderwritingController {

    private final UnderwritingService underwritingService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAnyAuthority('PERM_UNDERWRITING_QUEUE_READ','PERM_UNDERWRITING_READ','PERM_UNDERWRITING_ADMIN')")
    @GetMapping("/queue/summary")
    public ResponseEntity<ApiResponse<UwQueueSummaryDto>> queueSummary() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-001", underwritingService.queueSummary()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_UNDERWRITING_QUEUE_READ','PERM_UNDERWRITING_READ','PERM_UNDERWRITING_ADMIN')")
    @GetMapping("/cases")
    public ResponseEntity<ApiResponse<PagedData<UnderwritingCaseDto>>> searchCases(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String assignee,
            @RequestParam(required = false) String lob,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-002",
                underwritingService.searchCases(status, assignee, lob, search, page, size)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_UNDERWRITING_QUEUE_WRITE','PERM_UNDERWRITING_WRITE','PERM_UNDERWRITING_ADMIN','PERM_POLICY_WRITE','PERM_POLICY_ADMIN')")
    @PostMapping("/cases/submit")
    public ResponseEntity<ApiResponse<UnderwritingCaseDto>> submit(@RequestBody SubmitForUnderwritingRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-003",
                underwritingService.submitForUnderwriting(request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_UNDERWRITING_QUEUE_READ','PERM_UNDERWRITING_READ','PERM_UNDERWRITING_ADMIN')")
    @GetMapping("/cases/{id}")
    public ResponseEntity<ApiResponse<UnderwritingCaseDto>> getCase(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-004", underwritingService.getCase(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_UNDERWRITING_QUEUE_READ','PERM_UNDERWRITING_READ','PERM_UNDERWRITING_ADMIN','PERM_POLICY_READ')")
    @GetMapping("/cases/by-policy/{policyId}")
    public ResponseEntity<ApiResponse<UnderwritingCaseDto>> getByPolicy(@PathVariable UUID policyId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-005",
                underwritingService.getCaseByPolicy(policyId)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_UNDERWRITING_QUEUE_WRITE','PERM_UNDERWRITING_WRITE','PERM_UNDERWRITING_ADMIN')")
    @PostMapping("/cases/{id}/assign")
    public ResponseEntity<ApiResponse<UnderwritingCaseDto>> assign(@PathVariable UUID id, @RequestBody UwAssignRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-006", underwritingService.assign(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_UNDERWRITER_DECISION_WRITE','PERM_UNDERWRITING_WRITE','PERM_UNDERWRITING_ADMIN')")
    @PostMapping("/cases/{id}/decisions")
    public ResponseEntity<ApiResponse<UnderwritingDecisionDto>> decide(@PathVariable UUID id, @RequestBody UwDecisionRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-007",
                underwritingService.recordDecision(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_UNDERWRITER_DECISION_READ','PERM_UNDERWRITING_READ','PERM_UNDERWRITING_ADMIN')")
    @GetMapping("/cases/{id}/decisions")
    public ResponseEntity<ApiResponse<List<UnderwritingDecisionDto>>> listDecisions(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-008", underwritingService.listDecisions(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_UNDERWRITING_APPROVAL_WRITE','PERM_UNDERWRITING_WRITE','PERM_UNDERWRITING_ADMIN')")
    @PostMapping("/cases/{caseId}/approvals/{stepId}/approve")
    public ResponseEntity<ApiResponse<UwApprovalStepDto>> approveStep(
            @PathVariable UUID caseId, @PathVariable UUID stepId, @RequestBody(required = false) UwApprovalActionRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-009",
                underwritingService.approveStep(caseId, stepId, request != null ? request : new UwApprovalActionRequest())));
    }

    @PreAuthorize("hasAnyAuthority('PERM_UNDERWRITING_APPROVAL_WRITE','PERM_UNDERWRITING_WRITE','PERM_UNDERWRITING_ADMIN')")
    @PostMapping("/cases/{caseId}/approvals/{stepId}/reject")
    public ResponseEntity<ApiResponse<UwApprovalStepDto>> rejectStep(
            @PathVariable UUID caseId, @PathVariable UUID stepId, @RequestBody(required = false) UwApprovalActionRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-010",
                underwritingService.rejectStep(caseId, stepId, request != null ? request : new UwApprovalActionRequest())));
    }

    @PreAuthorize("hasAnyAuthority('PERM_UNDERWRITING_APPROVAL_READ','PERM_UNDERWRITING_READ','PERM_UNDERWRITING_ADMIN')")
    @GetMapping("/cases/{id}/approvals")
    public ResponseEntity<ApiResponse<List<UwApprovalStepDto>>> listApprovals(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-011", underwritingService.listApprovals(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_RISK_ASSESSMENT_WRITE','PERM_MEDICAL_ASSESSMENT_WRITE','PERM_VEHICLE_INSPECTION_WRITE','PERM_PROPERTY_INSPECTION_WRITE','PERM_SURVEY_REPORTS_WRITE','PERM_REINSURANCE_REVIEW_WRITE','PERM_UNDERWRITING_WRITE','PERM_UNDERWRITING_ADMIN')")
    @PostMapping("/cases/{id}/assessments")
    public ResponseEntity<ApiResponse<UwAssessmentDto>> createAssessment(@PathVariable UUID id, @RequestBody UwAssessmentRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-012",
                underwritingService.createAssessment(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_RISK_ASSESSMENT_WRITE','PERM_UNDERWRITING_WRITE','PERM_UNDERWRITING_ADMIN')")
    @PutMapping("/assessments/{assessmentId}")
    public ResponseEntity<ApiResponse<UwAssessmentDto>> updateAssessment(
            @PathVariable UUID assessmentId, @RequestBody UwAssessmentRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-013",
                underwritingService.updateAssessment(assessmentId, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_RISK_ASSESSMENT_READ','PERM_UNDERWRITING_READ','PERM_UNDERWRITING_ADMIN')")
    @GetMapping("/cases/{id}/assessments")
    public ResponseEntity<ApiResponse<List<UwAssessmentDto>>> listAssessments(
            @PathVariable UUID id, @RequestParam(required = false) String type) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-014",
                underwritingService.listAssessments(id, type)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_RISK_ASSESSMENT_READ','PERM_MEDICAL_ASSESSMENT_READ','PERM_VEHICLE_INSPECTION_READ','PERM_PROPERTY_INSPECTION_READ','PERM_SURVEY_REPORTS_READ','PERM_REINSURANCE_REVIEW_READ','PERM_RISK_SCORE_READ','PERM_UNDERWRITING_READ','PERM_UNDERWRITING_ADMIN')")
    @GetMapping("/assessments")
    public ResponseEntity<ApiResponse<PagedData<UwAssessmentDto>>> listByType(
            @RequestParam String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-015",
                underwritingService.listAssessmentsByType(type, page, size)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_RISK_SCORE_WRITE','PERM_RISK_ASSESSMENT_WRITE','PERM_UNDERWRITING_WRITE','PERM_UNDERWRITING_ADMIN')")
    @PostMapping("/cases/{id}/risk-score")
    public ResponseEntity<ApiResponse<UwAssessmentDto>> computeRiskScore(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-016",
                underwritingService.computeRiskScore(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_UNDERWRITING_READ','PERM_POLICY_READ','PERM_UNDERWRITING_ADMIN')")
    @GetMapping("/products/{productCode}/required")
    public ResponseEntity<ApiResponse<Boolean>> requiresUnderwriting(@PathVariable String productCode) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("UW-SUC-017",
                underwritingService.requiresUnderwriting(productCode)));
    }
}
