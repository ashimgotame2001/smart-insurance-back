package com.project.smartinsurance.billingService.service;

import com.project.smartinsurance.billingService.dto.*;
import com.project.smartinsurance.policyService.dto.PolicyPremiumDto;

import java.util.List;
import java.util.UUID;

public interface PremiumBillingService {
    BillingDeskSummaryDto getDeskSummary();
    List<PolicyPremiumDto> listSchedules(UUID policyId);
    List<PolicyPremiumDto> listInstallments(String status, int limit);
    OutstandingAgingDto getOutstanding(int limit);
    CollectionResultDto collect(CollectionRequest request);
    List<BillingPaymentDto> listPayments();
    GenerateInvoicesResultDto generateInvoices(GenerateInvoicesRequest request);
    List<PremiumAdjustmentDto> listAdjustments();
    PremiumAdjustmentDto createAdjustment(PremiumAdjustmentDto dto);
    PremiumAdjustmentDto approveAdjustment(UUID id);
    PremiumAdjustmentDto rejectAdjustment(UUID id);
    BillingReconcileDto getReconcileSnapshot();
}
