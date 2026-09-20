package com.project.smartinsurance.billingService.service;

import com.project.smartinsurance.billingService.dto.*;
import com.project.smartinsurance.policyService.dto.PolicyPremiumDto;

import java.util.List;
import java.util.UUID;

public interface PremiumLifecycleService {
    ScheduleGenerateResultDto generateSchedule(UUID policyId, boolean force);
    PremiumCalcResultDto calculate(PremiumCalcRequest request);
    List<PremiumCalcRuleDto> listCalcRules();
    PremiumCalcRuleDto createCalcRule(PremiumCalcRuleDto dto);
    PremiumCalcRuleDto updateCalcRule(UUID id, PremiumCalcRuleDto dto);
    void deleteCalcRule(UUID id);
    List<PremiumRefundDto> listRefunds();
    PremiumRefundDto createRefund(PremiumRefundDto dto);
    PremiumRefundDto approveRefund(UUID id, String approvedBy);
    PremiumRefundDto rejectRefund(UUID id);
    PremiumRefundDto completeRefund(UUID id);
    PremiumStatementDto getStatement(UUID policyId);
    PremiumJobResultDto markOverdue(int graceDays);
    PremiumJobResultDto runDunning();
    PremiumJobResultDto runLapseCheck(int graceDays);
    ScheduleGenerateResultDto generateBackPremiumOnReinstate(UUID policyId);
    PremiumDayEndDto dayEndSummary(UUID branchId);
    void applyEndorsementDelta(UUID policyId, java.math.BigDecimal deltaPremium, String note);
    void cancelOpenInstallments(UUID policyId);
}
