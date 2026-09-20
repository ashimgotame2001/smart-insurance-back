package com.project.smartinsurance.claimsService.controller;

import com.project.smartinsurance.claimsService.dto.*;
import com.project.smartinsurance.claimsService.service.ClaimService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAnyAuthority('PERM_CLAIMS_DASHBOARD_READ','PERM_CLAIMS_MANAGEMENT_READ','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @GetMapping("/dashboard-summary")
    public ResponseEntity<ApiResponse<ClaimDashboardSummaryDto>> dashboardSummary() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-025", claimService.getDashboardSummary()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIMS_MANAGEMENT_READ','PERM_REGISTER_CLAIM_READ','PERM_CLAIM_DOC_VERIFY_READ','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @GetMapping("/checklist-templates")
    public ResponseEntity<ApiResponse<List<ClaimChecklistTemplateDto>>> checklistTemplates() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-024", claimService.listChecklistTemplates()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_REGISTER_CLAIM_WRITE','PERM_CLAIMS_MANAGEMENT_WRITE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ClaimDto>> register(@RequestBody @Valid ClaimCreateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-001", claimService.registerClaim(request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIMS_MANAGEMENT_READ','PERM_REGISTER_CLAIM_READ','PERM_CLAIM_HISTORY_READ','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PagedData<ClaimDto>>> search(@RequestBody ClaimSearchRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-005", claimService.search(request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIMS_MANAGEMENT_READ','PERM_REGISTER_CLAIM_READ','PERM_CLAIM_HISTORY_READ','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedData<ClaimDto>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-006", claimService.list(page, size, sortBy, sortDir)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIMS_MANAGEMENT_READ','PERM_REGISTER_CLAIM_READ','PERM_CLAIM_HISTORY_READ','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @GetMapping("/number/{claimNumber}")
    public ResponseEntity<ApiResponse<ClaimDto>> getByNumber(@PathVariable String claimNumber) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-004", claimService.getByClaimNumber(claimNumber)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIMS_MANAGEMENT_READ','PERM_REGISTER_CLAIM_READ','PERM_CLAIM_HISTORY_READ','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClaimDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-003", claimService.getById(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_REGISTER_CLAIM_UPDATE','PERM_CLAIMS_MANAGEMENT_UPDATE','PERM_CLAIMS_MANAGEMENT_WRITE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClaimDto>> update(@PathVariable UUID id, @RequestBody ClaimUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-002", claimService.updateClaim(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIMS_MANAGEMENT_DELETE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        claimService.softDelete(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-007", null));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIM_VERIFICATION_WRITE','PERM_CLAIM_VERIFICATION_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PutMapping("/{id}/verify")
    public ResponseEntity<ApiResponse<ClaimDto>> verify(@PathVariable UUID id, @RequestBody ClaimVerifyRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-008", claimService.verify(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIM_DOC_VERIFY_WRITE','PERM_REGISTER_CLAIM_WRITE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/documents")
    public ResponseEntity<ApiResponse<ClaimDocumentDto>> addDocument(@PathVariable UUID id, @RequestBody @Valid ClaimDocumentRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-009", claimService.addDocument(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIM_DOC_VERIFY_WRITE','PERM_CLAIM_DOC_VERIFY_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PutMapping("/{id}/documents/{docId}/verify")
    public ResponseEntity<ApiResponse<ClaimDocumentDto>> verifyDocument(
            @PathVariable UUID id, @PathVariable UUID docId, @RequestBody ClaimDocumentVerifyRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-010", claimService.verifyDocument(id, docId, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIM_INVESTIGATION_WRITE','PERM_CLAIM_INVESTIGATION_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/investigate")
    public ResponseEntity<ApiResponse<ClaimInvestigationDto>> investigate(@PathVariable UUID id, @RequestBody ClaimInvestigationRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-011", claimService.investigate(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_FRAUD_INVESTIGATION_WRITE','PERM_FRAUD_INVESTIGATION_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/fraud")
    public ResponseEntity<ApiResponse<ClaimFraudDto>> fraud(@PathVariable UUID id, @RequestBody ClaimFraudRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-012", claimService.fraud(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_SURVEY_ASSIGNMENT_WRITE','PERM_SURVEY_ASSIGNMENT_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/survey")
    public ResponseEntity<ApiResponse<ClaimSurveyDto>> survey(@PathVariable UUID id, @RequestBody ClaimSurveyRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-013", claimService.survey(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIM_MEDICAL_REVIEW_WRITE','PERM_CLAIM_MEDICAL_REVIEW_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/medical-review")
    public ResponseEntity<ApiResponse<ClaimMedicalReviewDto>> medicalReview(@PathVariable UUID id, @RequestBody ClaimMedicalReviewRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-014", claimService.medicalReview(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIM_APPROVAL_WRITE','PERM_CLAIM_APPROVAL_MAKER','PERM_CLAIM_APPROVAL_CHECKER','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<ClaimDto>> approve(@PathVariable UUID id, @RequestBody ClaimDecisionRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-015", claimService.approve(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIM_REJECTION_WRITE','PERM_CLAIM_REJECTION_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ClaimDto>> reject(@PathVariable UUID id, @RequestBody ClaimDecisionRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-016", claimService.reject(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIM_SETTLEMENT_WRITE','PERM_CLAIM_SETTLEMENT_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/settle")
    public ResponseEntity<ApiResponse<ClaimDto>> settle(@PathVariable UUID id, @RequestBody ClaimSettlementRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-017", claimService.settle(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIM_PAYMENT_WRITE','PERM_CLAIM_PAYMENT_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/payments")
    public ResponseEntity<ApiResponse<ClaimPaymentDto>> payment(@PathVariable UUID id, @RequestBody ClaimPaymentRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-018", claimService.addPayment(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIM_RECOVERY_WRITE','PERM_CLAIM_RECOVERY_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/recoveries")
    public ResponseEntity<ApiResponse<ClaimRecoveryDto>> recovery(@PathVariable UUID id, @RequestBody ClaimRecoveryRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-019", claimService.addRecovery(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIM_SALVAGE_WRITE','PERM_CLAIM_SALVAGE_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/salvage")
    public ResponseEntity<ApiResponse<ClaimSalvageDto>> salvage(@PathVariable UUID id, @RequestBody ClaimSalvageRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-020", claimService.addSalvage(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIMS_MANAGEMENT_WRITE','PERM_CLAIMS_MANAGEMENT_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/reserves")
    public ResponseEntity<ApiResponse<ClaimReserveDto>> reserve(@PathVariable UUID id, @RequestBody ClaimReserveRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-021", claimService.reviseReserve(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIMS_MANAGEMENT_WRITE','PERM_CLAIMS_MANAGEMENT_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/reopen")
    public ResponseEntity<ApiResponse<ClaimDto>> reopen(@PathVariable UUID id, @RequestBody ClaimReopenRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-022", claimService.reopen(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIMS_MANAGEMENT_WRITE','PERM_CLAIMS_MANAGEMENT_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/hold")
    public ResponseEntity<ApiResponse<ClaimDto>> hold(@PathVariable UUID id, @RequestBody ClaimHoldRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-023", claimService.hold(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIMS_MANAGEMENT_WRITE','PERM_CLAIMS_MANAGEMENT_UPDATE','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @PostMapping("/{id}/resume")
    public ResponseEntity<ApiResponse<ClaimDto>> resume(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-023", claimService.resume(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_CLAIMS_MANAGEMENT_READ','PERM_CLAIM_HISTORY_READ','PERM_CLAIMS_MANAGEMENT_ADMIN')")
    @GetMapping("/{id}/events")
    public ResponseEntity<ApiResponse<List<ClaimEventDto>>> events(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CLM-SUC-024", claimService.getEvents(id)));
    }
}
