package com.project.smartinsurance.policyService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.policyService.dto.*;
import com.project.smartinsurance.policyService.service.PolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_POLICY_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<PolicyDto>> createPolicy(@RequestBody @Valid PolicyCreateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-001", policyService.createPolicy(request)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PolicyDto>> updatePolicy(@PathVariable UUID id, @RequestBody PolicyUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-002", policyService.updatePolicy(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePolicy(@PathVariable UUID id) {
        policyService.deletePolicy(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-003", null));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PolicyDto>> getPolicyById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-004", policyService.getPolicyById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_READ')")
    @GetMapping("/number/{policyNumber}")
    public ResponseEntity<ApiResponse<PolicyDto>> getPolicyByNumber(@PathVariable String policyNumber) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-005", policyService.getPolicyByNumber(policyNumber)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_READ')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PagedData<PolicyDto>>> searchPolicies(@RequestBody PolicySearchRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-006", policyService.searchPolicies(request)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedData<PolicyDto>>> getAllPolicies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-007", policyService.getAllPolicies(page, size, sortBy, sortDir)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_READ')")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<PagedData<PolicyDto>>> getPoliciesByCustomer(
            @PathVariable UUID customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-008", policyService.getPoliciesByCustomer(customerId, page, size, sortBy, sortDir)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_READ')")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<PagedData<PolicyDto>>> getPoliciesByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-009", policyService.getPoliciesByStatus(status, page, size, sortBy, sortDir)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<PolicyDto>> approvePolicy(@PathVariable UUID id, @RequestParam String approvedBy) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-010", policyService.approvePolicy(id, approvedBy)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_WRITE')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<PolicyDto>> cancelPolicy(@PathVariable UUID id, @RequestBody @Valid CancelPolicyRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-011", policyService.cancelPolicy(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_WRITE')")
    @PutMapping("/{id}/reinstate")
    public ResponseEntity<ApiResponse<PolicyDto>> reinstatePolicy(@PathVariable UUID id, @RequestBody @Valid ReinstatePolicyRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-012", policyService.reinstatePolicy(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_WRITE')")
    @PostMapping("/{id}/issue")
    public ResponseEntity<ApiResponse<PolicyDto>> issuePolicy(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-022", policyService.issuePolicy(id)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_WRITE')")
    @PutMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<PolicyDto>> suspendPolicy(@PathVariable UUID id, @RequestBody @Valid CancelPolicyRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-023", policyService.suspendPolicy(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_WRITE')")
    @PutMapping("/{id}/transfer")
    public ResponseEntity<ApiResponse<PolicyDto>> transferPolicy(@PathVariable UUID id, @RequestBody @Valid TransferPolicyRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-024", policyService.transferPolicy(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_WRITE')")
    @PostMapping("/{id}/renew")
    public ResponseEntity<ApiResponse<PolicyDto>> renewPolicy(@PathVariable UUID id, @RequestBody @Valid RenewPolicyRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-025", policyService.renewPolicy(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_WRITE')")
    @PutMapping("/{id}/upgrade")
    public ResponseEntity<ApiResponse<PolicyDto>> upgradePolicy(@PathVariable UUID id, @RequestBody @Valid UpgradePolicyRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-026", policyService.upgradePolicy(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_WRITE')")
    @PutMapping("/{id}/downgrade")
    public ResponseEntity<ApiResponse<PolicyDto>> downgradePolicy(@PathVariable UUID id, @RequestBody @Valid DowngradePolicyRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-027", policyService.downgradePolicy(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_WRITE')")
    @PostMapping("/{policyId}/endorsements")
    public ResponseEntity<ApiResponse<EndorsementDto>> createEndorsement(@PathVariable UUID policyId, @RequestBody @Valid EndorsementRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-013", policyService.createEndorsement(policyId, request)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_ADMIN')")
    @PutMapping("/endorsements/{endorsementId}/approve")
    public ResponseEntity<ApiResponse<EndorsementDto>> approveEndorsement(@PathVariable UUID endorsementId, @RequestParam String approvedBy) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-014", policyService.approveEndorsement(endorsementId, approvedBy)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_ADMIN')")
    @PutMapping("/endorsements/{endorsementId}/reject")
    public ResponseEntity<ApiResponse<EndorsementDto>> rejectEndorsement(@PathVariable UUID endorsementId, @RequestParam String rejectedBy, @RequestParam String reason) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-015", policyService.rejectEndorsement(endorsementId, rejectedBy, reason)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_READ')")
    @GetMapping("/{policyId}/endorsements")
    public ResponseEntity<ApiResponse<List<EndorsementDto>>> getEndorsementsByPolicy(@PathVariable UUID policyId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-016", policyService.getEndorsementsByPolicy(policyId)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_READ')")
    @GetMapping("/{policyId}/coverages")
    public ResponseEntity<ApiResponse<List<CoverageDto>>> getCoveragesByPolicy(@PathVariable UUID policyId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-017", policyService.getCoveragesByPolicy(policyId)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_READ')")
    @GetMapping("/{policyId}/premiums")
    public ResponseEntity<ApiResponse<List<PolicyPremiumDto>>> getPremiumsByPolicy(@PathVariable UUID policyId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-018", policyService.getPremiumsByPolicy(policyId)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_READ')")
    @GetMapping("/{policyId}/versions")
    public ResponseEntity<ApiResponse<List<PolicyVersionDto>>> getVersionsByPolicy(@PathVariable UUID policyId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-019", policyService.getVersionsByPolicy(policyId)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_READ')")
    @GetMapping("/audit")
    public ResponseEntity<ApiResponse<List<PolicyAuditEntryDto>>> getAuditTrail(@RequestParam String entityType, @RequestParam String entityId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-020", policyService.getAuditTrail(entityType, entityId)));
    }

    @PreAuthorize("hasAuthority('PERM_POLICY_READ')")
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<PolicySummaryResponse>> getPolicySummary() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("POL-SUC-021", policyService.getPolicySummary()));
    }
}
