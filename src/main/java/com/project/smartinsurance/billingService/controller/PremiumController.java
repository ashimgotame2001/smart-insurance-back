package com.project.smartinsurance.billingService.controller;

import com.project.smartinsurance.billingService.dto.*;
import com.project.smartinsurance.billingService.service.PremiumBillingService;
import com.project.smartinsurance.billingService.service.PremiumLifecycleService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.policyService.dto.PolicyPremiumDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/premium")
@RequiredArgsConstructor
public class PremiumController {

    private final PremiumLifecycleService lifecycleService;
    private final PremiumBillingService billingService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_MANAGEMENT_READ','PERM_PREMIUM_COLLECTION_READ','PERM_BILLING_PAYMENTS_READ')")
    @GetMapping("/desk/summary")
    public ResponseEntity<ApiResponse<BillingDeskSummaryDto>> deskSummary() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-080", billingService.getDeskSummary()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_CALCULATION_WRITE','PERM_PREMIUM_CALCULATION_READ','PERM_PREMIUM_MANAGEMENT_WRITE')")
    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<PremiumCalcResultDto>> calculate(@RequestBody PremiumCalcRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-081", lifecycleService.calculate(request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_CALC_RULES_READ','PERM_PREMIUM_MANAGEMENT_READ')")
    @GetMapping("/calc-rules")
    public ResponseEntity<ApiResponse<List<PremiumCalcRuleDto>>> listCalcRules() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-082", lifecycleService.listCalcRules()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_CALC_RULES_WRITE','PERM_PREMIUM_MANAGEMENT_WRITE')")
    @PostMapping("/calc-rules")
    public ResponseEntity<ApiResponse<PremiumCalcRuleDto>> createCalcRule(@RequestBody PremiumCalcRuleDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-083", lifecycleService.createCalcRule(dto)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_CALC_RULES_WRITE','PERM_PREMIUM_MANAGEMENT_WRITE')")
    @PutMapping("/calc-rules/{id}")
    public ResponseEntity<ApiResponse<PremiumCalcRuleDto>> updateCalcRule(@PathVariable UUID id, @RequestBody PremiumCalcRuleDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-084", lifecycleService.updateCalcRule(id, dto)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_CALC_RULES_DELETE','PERM_PREMIUM_MANAGEMENT_ADMIN')")
    @DeleteMapping("/calc-rules/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCalcRule(@PathVariable UUID id) {
        lifecycleService.deleteCalcRule(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-085", null));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_SCHEDULE_WRITE','PERM_PREMIUM_MANAGEMENT_WRITE','PERM_BILLING_PAYMENTS_WRITE')")
    @PostMapping("/schedules/generate/{policyId}")
    public ResponseEntity<ApiResponse<ScheduleGenerateResultDto>> generateSchedule(
            @PathVariable UUID policyId,
            @RequestParam(defaultValue = "false") boolean force) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse(
                "BIL-SUC-086", lifecycleService.generateSchedule(policyId, force)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_SCHEDULE_READ','PERM_PREMIUM_INSTALLMENTS_READ','PERM_BILLING_PAYMENTS_READ')")
    @GetMapping("/schedules")
    public ResponseEntity<ApiResponse<List<PolicyPremiumDto>>> schedules(@RequestParam(required = false) UUID policyId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-087", billingService.listSchedules(policyId)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_INSTALLMENTS_READ','PERM_BILLING_PAYMENTS_READ')")
    @GetMapping("/installments")
    public ResponseEntity<ApiResponse<List<PolicyPremiumDto>>> installments(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "200") int limit) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-088", billingService.listInstallments(status, limit)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_OUTSTANDING_PREMIUM_READ','PERM_PREMIUM_MANAGEMENT_READ','PERM_BILLING_PAYMENTS_READ')")
    @GetMapping("/outstanding")
    public ResponseEntity<ApiResponse<OutstandingAgingDto>> outstanding(@RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-089", billingService.getOutstanding(limit)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_COLLECTION_WRITE','PERM_PAYMENT_COLLECTION_WRITE','PERM_BILLING_PAYMENTS_WRITE')")
    @PostMapping("/collections")
    public ResponseEntity<ApiResponse<CollectionResultDto>> collect(@RequestBody CollectionRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-090", billingService.collect(request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_HISTORY_READ','PERM_PAYMENT_HISTORY_READ','PERM_BILLING_PAYMENTS_READ')")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<BillingPaymentDto>>> history() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-091", billingService.listPayments()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_ADJUSTMENT_READ','PERM_PREMIUM_WAIVER_READ','PERM_BILLING_PAYMENTS_READ')")
    @GetMapping("/adjustments")
    public ResponseEntity<ApiResponse<List<PremiumAdjustmentDto>>> adjustments() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-092", billingService.listAdjustments()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_ADJUSTMENT_WRITE','PERM_BILLING_PAYMENTS_WRITE')")
    @PostMapping("/adjustments")
    public ResponseEntity<ApiResponse<PremiumAdjustmentDto>> createAdjustment(@RequestBody PremiumAdjustmentDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-093", billingService.createAdjustment(dto)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_WAIVER_WRITE','PERM_PREMIUM_ADJUSTMENT_WRITE','PERM_BILLING_PAYMENTS_WRITE')")
    @PostMapping("/waivers")
    public ResponseEntity<ApiResponse<PremiumAdjustmentDto>> createWaiver(@RequestBody PremiumAdjustmentDto dto) {
        dto.setAdjustmentType("WAIVER");
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-094", billingService.createAdjustment(dto)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_REFUND_READ','PERM_PREMIUM_MANAGEMENT_READ')")
    @GetMapping("/refunds")
    public ResponseEntity<ApiResponse<List<PremiumRefundDto>>> listRefunds() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-095", lifecycleService.listRefunds()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_REFUND_WRITE','PERM_PREMIUM_MANAGEMENT_WRITE')")
    @PostMapping("/refunds")
    public ResponseEntity<ApiResponse<PremiumRefundDto>> createRefund(@RequestBody PremiumRefundDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-096", lifecycleService.createRefund(dto)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_REFUND_CHECKER','PERM_PREMIUM_REFUND_ADMIN','PERM_PREMIUM_MANAGEMENT_ADMIN')")
    @PostMapping("/refunds/{id}/approve")
    public ResponseEntity<ApiResponse<PremiumRefundDto>> approveRefund(
            @PathVariable UUID id,
            @RequestParam(required = false) String approvedBy) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse(
                "BIL-SUC-097", lifecycleService.approveRefund(id, approvedBy != null ? approvedBy : "SYSTEM")));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_REFUND_CHECKER','PERM_PREMIUM_REFUND_ADMIN','PERM_PREMIUM_MANAGEMENT_ADMIN')")
    @PostMapping("/refunds/{id}/reject")
    public ResponseEntity<ApiResponse<PremiumRefundDto>> rejectRefund(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-098", lifecycleService.rejectRefund(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_REFUND_WRITE','PERM_PREMIUM_REFUND_ADMIN','PERM_PREMIUM_MANAGEMENT_ADMIN')")
    @PostMapping("/refunds/{id}/complete")
    public ResponseEntity<ApiResponse<PremiumRefundDto>> completeRefund(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-099", lifecycleService.completeRefund(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_HISTORY_READ','PERM_PREMIUM_MANAGEMENT_READ','PERM_BILLING_PAYMENTS_READ')")
    @GetMapping("/statements/{policyId}")
    public ResponseEntity<ApiResponse<PremiumStatementDto>> statement(@PathVariable UUID policyId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-100", lifecycleService.getStatement(policyId)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_MANAGEMENT_ADMIN','PERM_PREMIUM_MANAGEMENT_WRITE')")
    @PostMapping("/jobs/mark-overdue")
    public ResponseEntity<ApiResponse<PremiumJobResultDto>> markOverdue(@RequestParam(defaultValue = "15") int graceDays) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-101", lifecycleService.markOverdue(graceDays)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_MANAGEMENT_ADMIN','PERM_PREMIUM_MANAGEMENT_WRITE')")
    @PostMapping("/jobs/dunning")
    public ResponseEntity<ApiResponse<PremiumJobResultDto>> runDunning() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-102", lifecycleService.runDunning()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_MANAGEMENT_ADMIN','PERM_PREMIUM_MANAGEMENT_WRITE')")
    @PostMapping("/jobs/lapse-check")
    public ResponseEntity<ApiResponse<PremiumJobResultDto>> runLapseCheck(@RequestParam(defaultValue = "15") int graceDays) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-103", lifecycleService.runLapseCheck(graceDays)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_MANAGEMENT_WRITE','PERM_PREMIUM_SCHEDULE_WRITE')")
    @PostMapping("/reinstate/{policyId}/back-premium")
    public ResponseEntity<ApiResponse<ScheduleGenerateResultDto>> backPremium(@PathVariable UUID policyId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse(
                "BIL-SUC-104", lifecycleService.generateBackPremiumOnReinstate(policyId)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_COLLECTION_READ','PERM_PREMIUM_MANAGEMENT_READ','PERM_BILLING_PAYMENTS_READ')")
    @GetMapping("/day-end")
    public ResponseEntity<ApiResponse<PremiumDayEndDto>> dayEnd(@RequestParam(required = false) UUID branchId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-105", lifecycleService.dayEndSummary(branchId)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_ADJUSTMENT_WRITE','PERM_PREMIUM_MANAGEMENT_WRITE')")
    @PostMapping("/endorsements/{policyId}/delta")
    public ResponseEntity<ApiResponse<Map<String, Object>>> endorsementDelta(
            @PathVariable UUID policyId,
            @RequestBody Map<String, Object> body) {
        BigDecimal delta = body.get("deltaPremium") != null
                ? new BigDecimal(body.get("deltaPremium").toString())
                : BigDecimal.ZERO;
        String note = body.get("note") != null ? body.get("note").toString() : null;
        lifecycleService.applyEndorsementDelta(policyId, delta, note);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse(
                "BIL-SUC-106", Map.of("policyId", policyId.toString(), "deltaPremium", delta, "applied", true)));
    }
}
