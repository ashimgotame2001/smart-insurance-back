package com.project.smartinsurance.billingService.controller;

import com.project.smartinsurance.billingService.dto.*;
import com.project.smartinsurance.billingService.service.PremiumBillingService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.policyService.dto.PolicyPremiumDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class BillingOpsController {

    private final PremiumBillingService premiumBillingService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAnyAuthority('PERM_BILLING_PAYMENTS_READ','PERM_PAYMENT_COLLECTION_READ','PERM_INVOICES_READ')")
    @GetMapping("/desk/summary")
    public ResponseEntity<ApiResponse<BillingDeskSummaryDto>> deskSummary() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-060", premiumBillingService.getDeskSummary()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_BILLING_PAYMENTS_READ','PERM_PREMIUM_SCHEDULE_READ','PERM_PREMIUM_INSTALLMENTS_READ')")
    @GetMapping("/schedules")
    public ResponseEntity<ApiResponse<List<PolicyPremiumDto>>> schedules(@RequestParam(required = false) UUID policyId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-061", premiumBillingService.listSchedules(policyId)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_BILLING_PAYMENTS_READ','PERM_PREMIUM_INSTALLMENTS_READ')")
    @GetMapping("/installments")
    public ResponseEntity<ApiResponse<List<PolicyPremiumDto>>> installments(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "200") int limit) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-062", premiumBillingService.listInstallments(status, limit)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_BILLING_PAYMENTS_READ','PERM_OUTSTANDING_PREMIUM_READ')")
    @GetMapping("/outstanding")
    public ResponseEntity<ApiResponse<OutstandingAgingDto>> outstanding(@RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-063", premiumBillingService.getOutstanding(limit)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PAYMENT_COLLECTION_WRITE','PERM_BILLING_PAYMENTS_WRITE')")
    @PostMapping("/collections")
    public ResponseEntity<ApiResponse<CollectionResultDto>> collect(@RequestBody CollectionRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-064", premiumBillingService.collect(request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PAYMENT_HISTORY_READ','PERM_PAYMENT_COLLECTION_READ','PERM_BILLING_PAYMENTS_READ')")
    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<List<BillingPaymentDto>>> payments() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-065", premiumBillingService.listPayments()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_INVOICES_WRITE','PERM_BILLING_PAYMENTS_WRITE')")
    @PostMapping("/invoices/generate")
    public ResponseEntity<ApiResponse<GenerateInvoicesResultDto>> generateInvoices(@RequestBody(required = false) GenerateInvoicesRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse(
                "BIL-SUC-066",
                premiumBillingService.generateInvoices(request != null ? request : new GenerateInvoicesRequest())));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_ADJUSTMENT_READ','PERM_PREMIUM_WAIVER_READ','PERM_BILLING_PAYMENTS_READ')")
    @GetMapping("/adjustments")
    public ResponseEntity<ApiResponse<List<PremiumAdjustmentDto>>> adjustments() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-067", premiumBillingService.listAdjustments()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_ADJUSTMENT_WRITE','PERM_PREMIUM_WAIVER_WRITE','PERM_BILLING_PAYMENTS_WRITE')")
    @PostMapping("/adjustments")
    public ResponseEntity<ApiResponse<PremiumAdjustmentDto>> createAdjustment(@RequestBody PremiumAdjustmentDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-068", premiumBillingService.createAdjustment(dto)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_ADJUSTMENT_CHECKER','PERM_PREMIUM_ADJUSTMENT_ADMIN','PERM_BILLING_PAYMENTS_ADMIN')")
    @PostMapping("/adjustments/{id}/approve")
    public ResponseEntity<ApiResponse<PremiumAdjustmentDto>> approveAdjustment(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-069", premiumBillingService.approveAdjustment(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PREMIUM_ADJUSTMENT_CHECKER','PERM_PREMIUM_ADJUSTMENT_ADMIN','PERM_BILLING_PAYMENTS_ADMIN')")
    @PostMapping("/adjustments/{id}/reject")
    public ResponseEntity<ApiResponse<PremiumAdjustmentDto>> rejectAdjustment(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-070", premiumBillingService.rejectAdjustment(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_PAYMENT_RECONCILIATION_READ','PERM_BILLING_PAYMENTS_READ')")
    @GetMapping("/reconcile/snapshot")
    public ResponseEntity<ApiResponse<BillingReconcileDto>> reconcileSnapshot() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-071", premiumBillingService.getReconcileSnapshot()));
    }
}
