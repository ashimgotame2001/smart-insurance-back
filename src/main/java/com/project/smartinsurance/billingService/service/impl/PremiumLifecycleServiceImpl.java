package com.project.smartinsurance.billingService.service.impl;

import com.project.smartinsurance.billingService.dto.*;
import com.project.smartinsurance.billingService.model.*;
import com.project.smartinsurance.billingService.model.BillingPayment.PaymentStatus;
import com.project.smartinsurance.billingService.model.PremiumRefund.ReasonCode;
import com.project.smartinsurance.billingService.model.PremiumRefund.RefundStatus;
import com.project.smartinsurance.billingService.model.enums.InstallmentPaymentStatus;
import com.project.smartinsurance.billingService.repository.*;
import com.project.smartinsurance.billingService.service.PremiumLifecycleService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.policyService.dto.PolicyPremiumDto;
import com.project.smartinsurance.policyService.model.Policy;
import com.project.smartinsurance.policyService.model.PolicyPremium;
import com.project.smartinsurance.policyService.model.enums.PolicyStatus;
import com.project.smartinsurance.policyService.repository.PolicyPremiumRepository;
import com.project.smartinsurance.policyService.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PremiumLifecycleServiceImpl implements PremiumLifecycleService {

    private final PolicyRepository policyRepository;
    private final PolicyPremiumRepository premiumRepository;
    private final PremiumCalcRuleRepository calcRuleRepository;
    private final PremiumCalculationRepository calculationRepository;
    private final PremiumRefundRepository refundRepository;
    private final DunningNoticeRepository dunningNoticeRepository;
    private final BillingPaymentRepository paymentRepository;
    private final ReceiptRepository receiptRepository;
    private final PremiumGlPostingService glPostingService;

    @Override
    @Transactional
    public ScheduleGenerateResultDto generateSchedule(UUID policyId, boolean force) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new GlobalException("POL-001", policyId));

        List<PolicyPremium> existing = premiumRepository.findByPolicyIdOrderByDueDateAsc(policyId);
        boolean hasSettled = existing.stream().anyMatch(this::isSettledOrPartial);
        if (!existing.isEmpty() && hasSettled && !force) {
            return ScheduleGenerateResultDto.builder()
                    .policyId(policyId)
                    .policyNumber(policy.getPolicyNumber())
                    .installmentCount(existing.size())
                    .totalPayable(existing.stream().map(this::payableOf).reduce(BigDecimal.ZERO, BigDecimal::add))
                    .frequency(policy.getPaymentFrequency())
                    .message("Schedule already has paid/partial installments — skipped. Use force or endorsement path.")
                    .build();
        }
        if (!existing.isEmpty() && !hasSettled) {
            existing.forEach(p -> {
                p.setStatus(Status.DELETED);
                premiumRepository.save(p);
            });
        }

        BigDecimal total = policy.getTotalPremium() != null ? policy.getTotalPremium()
                : (policy.getBasePremium() != null ? policy.getBasePremium() : BigDecimal.ZERO);
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            return ScheduleGenerateResultDto.builder()
                    .policyId(policyId)
                    .policyNumber(policy.getPolicyNumber())
                    .installmentCount(0)
                    .totalPayable(BigDecimal.ZERO)
                    .frequency(policy.getPaymentFrequency())
                    .message("Policy has no total premium — schedule not generated.")
                    .build();
        }

        String freq = normalizeFrequency(policy.getPaymentFrequency());
        int count = installmentCount(freq, 12);
        LocalDate start = policy.getEffectiveDate() != null ? policy.getEffectiveDate()
                : (policy.getIssueDate() != null ? policy.getIssueDate()
                : (policy.getInceptionDate() != null ? policy.getInceptionDate() : LocalDate.now()));

        BigDecimal each = total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        BigDecimal allocated = BigDecimal.ZERO;
        List<PolicyPremium> created = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            BigDecimal payable = (i == count) ? total.subtract(allocated) : each;
            allocated = allocated.add(payable);
            LocalDate due = dueDateFor(start, freq, i - 1);
            PolicyPremium row = PolicyPremium.builder()
                    .policy(policy)
                    .basePremium(payable)
                    .totalPremium(payable)
                    .payableAmount(payable)
                    .paidAmount(BigDecimal.ZERO)
                    .balanceAmount(payable)
                    .installmentNumber(i)
                    .dueDate(due)
                    .graceEndDate(due.plusDays(15))
                    .paymentStatus(InstallmentPaymentStatus.UNPAID.name())
                    .frequency(freq)
                    .currencyCode(policy.getCurrencyCode())
                    .lateFeeAmount(BigDecimal.ZERO)
                    .build();
            row.setStatus(Status.ACTIVE);
            created.add(premiumRepository.save(row));
        }

        return ScheduleGenerateResultDto.builder()
                .policyId(policyId)
                .policyNumber(policy.getPolicyNumber())
                .installmentCount(created.size())
                .totalPayable(total)
                .frequency(freq)
                .message("Schedule generated with " + created.size() + " installment(s).")
                .build();
    }

    @Override
    @Transactional
    public PremiumCalcResultDto calculate(PremiumCalcRequest request) {
        BigDecimal sumInsured = nz(request.getSumInsured());
        String freq = normalizeFrequency(request.getPaymentFrequency());
        int term = request.getTermYears() != null && request.getTermYears() > 0 ? request.getTermYears() : 1;

        PremiumCalcRule rule = resolveRule(request.getProductCode(), request.getPlanCode());
        BigDecimal baseRate = rule != null && rule.getBaseRate() != null ? rule.getBaseRate() : new BigDecimal("10");
        String rateType = rule != null && rule.getRateType() != null ? rule.getRateType() : "PER_MILLE";

        BigDecimal base;
        if ("FLAT".equalsIgnoreCase(rateType)) {
            base = baseRate;
        } else if ("PERCENT".equalsIgnoreCase(rateType)) {
            base = sumInsured.multiply(baseRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            // PER_MILLE: rate per 1000 SI
            base = sumInsured.multiply(baseRate).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
        }
        if (term > 1 && !"SINGLE".equals(freq)) {
            base = base.multiply(BigDecimal.valueOf(term)).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal loadingPct = request.getLoadingPercent() != null ? request.getLoadingPercent()
                : (rule != null && rule.getLoadingPercent() != null ? rule.getLoadingPercent() : BigDecimal.ZERO);
        BigDecimal surcharge = base.multiply(loadingPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal taxPct = rule != null && rule.getTaxPercent() != null ? rule.getTaxPercent() : BigDecimal.ZERO;
        BigDecimal tax = base.add(surcharge).multiply(taxPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal fee = rule != null && rule.getFeeAmount() != null ? rule.getFeeAmount() : BigDecimal.ZERO;
        BigDecimal total = base.add(surcharge).add(tax).add(fee);

        int payingTerm = rule != null && rule.getDefaultPayingTerm() != null ? rule.getDefaultPayingTerm() : 12;
        if (request.getTermYears() != null) payingTerm = Math.max(term * 12, 1);
        int installments = installmentCount(freq, payingTerm);

        PremiumCalculation calc = PremiumCalculation.builder()
                .calcRef(nextRef("CALC"))
                .productCode(request.getProductCode())
                .planCode(request.getPlanCode())
                .policyId(request.getPolicyId())
                .sumInsured(sumInsured)
                .termYears(term)
                .paymentFrequency(freq)
                .age(request.getAge())
                .loadingPercent(loadingPct)
                .basePremium(base)
                .taxAmount(tax)
                .surchargeAmount(surcharge)
                .feeAmount(fee)
                .totalPremium(total)
                .installmentCount(installments)
                .ruleCode(rule != null ? rule.getRuleCode() : "DEFAULT")
                .calculatedAt(LocalDateTime.now())
                .build();
        calc.setStatus(Status.ACTIVE);
        calc = calculationRepository.save(calc);

        return PremiumCalcResultDto.builder()
                .id(calc.getId())
                .calcRef(calc.getCalcRef())
                .productCode(calc.getProductCode())
                .planCode(calc.getPlanCode())
                .ruleCode(calc.getRuleCode())
                .sumInsured(sumInsured)
                .paymentFrequency(freq)
                .basePremium(base)
                .taxAmount(tax)
                .surchargeAmount(surcharge)
                .feeAmount(fee)
                .totalPremium(total)
                .installmentCount(installments)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PremiumCalcRuleDto> listCalcRules() {
        return calcRuleRepository.findAllByStatus(Status.ACTIVE).stream().map(this::toRuleDto).toList();
    }

    @Override
    @Transactional
    public PremiumCalcRuleDto createCalcRule(PremiumCalcRuleDto dto) {
        PremiumCalcRule entity = PremiumCalcRule.builder()
                .ruleCode(dto.getRuleCode() != null ? dto.getRuleCode() : nextRef("RULE"))
                .ruleName(dto.getRuleName())
                .productCode(dto.getProductCode())
                .planCode(dto.getPlanCode())
                .baseRate(dto.getBaseRate())
                .rateType(dto.getRateType() != null ? dto.getRateType() : "PER_MILLE")
                .taxPercent(dto.getTaxPercent())
                .loadingPercent(dto.getLoadingPercent())
                .feeAmount(dto.getFeeAmount())
                .defaultFrequency(dto.getDefaultFrequency())
                .defaultPayingTerm(dto.getDefaultPayingTerm())
                .description(dto.getDescription())
                .active(dto.getActive() == null || dto.getActive())
                .build();
        entity.setStatus(Status.ACTIVE);
        return toRuleDto(calcRuleRepository.save(entity));
    }

    @Override
    @Transactional
    public PremiumCalcRuleDto updateCalcRule(UUID id, PremiumCalcRuleDto dto) {
        PremiumCalcRule entity = calcRuleRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-020"));
        if (dto.getRuleName() != null) entity.setRuleName(dto.getRuleName());
        if (dto.getProductCode() != null) entity.setProductCode(dto.getProductCode());
        if (dto.getPlanCode() != null) entity.setPlanCode(dto.getPlanCode());
        if (dto.getBaseRate() != null) entity.setBaseRate(dto.getBaseRate());
        if (dto.getRateType() != null) entity.setRateType(dto.getRateType());
        if (dto.getTaxPercent() != null) entity.setTaxPercent(dto.getTaxPercent());
        if (dto.getLoadingPercent() != null) entity.setLoadingPercent(dto.getLoadingPercent());
        if (dto.getFeeAmount() != null) entity.setFeeAmount(dto.getFeeAmount());
        if (dto.getDefaultFrequency() != null) entity.setDefaultFrequency(dto.getDefaultFrequency());
        if (dto.getDefaultPayingTerm() != null) entity.setDefaultPayingTerm(dto.getDefaultPayingTerm());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getActive() != null) entity.setActive(dto.getActive());
        return toRuleDto(calcRuleRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteCalcRule(UUID id) {
        PremiumCalcRule entity = calcRuleRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-020"));
        entity.setStatus(Status.DELETED);
        entity.setActive(false);
        calcRuleRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PremiumRefundDto> listRefunds() {
        return refundRepository.findAllByStatus(Status.ACTIVE).stream()
                .sorted(Comparator.comparing(PremiumRefund::getRefundDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toRefundDto)
                .toList();
    }

    @Override
    @Transactional
    public PremiumRefundDto createRefund(PremiumRefundDto dto) {
        PremiumRefund entity = PremiumRefund.builder()
                .refundNo(nextRef("PREF"))
                .policyId(dto.getPolicyId())
                .policyNumber(dto.getPolicyNumber())
                .customerName(dto.getCustomerName())
                .amount(dto.getAmount() != null ? dto.getAmount() : BigDecimal.ZERO)
                .reasonCode(parseReason(dto.getReasonCode()))
                .reason(dto.getReason())
                .refundDate(dto.getRefundDate() != null ? dto.getRefundDate() : LocalDate.now())
                .refundStatus(RefundStatus.PENDING)
                .build();
        entity.setStatus(Status.ACTIVE);
        if (dto.getPolicyId() != null) {
            cancelOpenInstallments(dto.getPolicyId());
        }
        return toRefundDto(refundRepository.save(entity));
    }

    @Override
    @Transactional
    public PremiumRefundDto approveRefund(UUID id, String approvedBy) {
        PremiumRefund entity = refundRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-021"));
        entity.setRefundStatus(RefundStatus.APPROVED);
        entity.setApprovedBy(approvedBy);
        return toRefundDto(refundRepository.save(entity));
    }

    @Override
    @Transactional
    public PremiumRefundDto rejectRefund(UUID id) {
        PremiumRefund entity = refundRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-021"));
        entity.setRefundStatus(RefundStatus.REJECTED);
        return toRefundDto(refundRepository.save(entity));
    }

    @Override
    @Transactional
    public PremiumRefundDto completeRefund(UUID id) {
        PremiumRefund entity = refundRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-021"));
        entity.setRefundStatus(RefundStatus.COMPLETED);
        refundRepository.save(entity);
        glPostingService.postPremiumRefund(entity);
        return toRefundDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PremiumStatementDto getStatement(UUID policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new GlobalException("POL-001", policyId));
        List<PolicyPremium> installments = premiumRepository.findByPolicyIdOrderByDueDateAsc(policyId);
        List<BillingPayment> payments = paymentRepository.findByPolicyIdAndStatus(policyId, Status.ACTIVE);

        BigDecimal payable = installments.stream().map(this::payableOf).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal paid = installments.stream().map(p -> nz(p.getPaidAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal outstanding = installments.stream()
                .filter(p -> !isFullySettled(p))
                .map(this::balanceOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<PolicyPremiumDto> instDtos = installments.stream().map(p -> PolicyPremiumDto.builder()
                .id(p.getId())
                .policyId(policyId)
                .policyNumber(policy.getPolicyNumber())
                .customerName(policy.getCustomerName())
                .installmentNumber(p.getInstallmentNumber())
                .dueDate(p.getDueDate())
                .payableAmount(payableOf(p))
                .paidAmount(nz(p.getPaidAmount()))
                .balanceAmount(balanceOf(p))
                .paymentStatus(p.getPaymentStatus())
                .paidDate(p.getPaidDate())
                .build()).toList();

        List<BillingPaymentDto> payDtos = payments.stream().map(p -> BillingPaymentDto.builder()
                .id(p.getId())
                .paymentRef(p.getPaymentRef())
                .policyId(p.getPolicyId())
                .policyNumber(p.getPolicyNumber())
                .amount(p.getAmount())
                .paymentMode(p.getPaymentMode())
                .paymentDate(p.getPaymentDate())
                .receiptNo(p.getReceiptNo())
                .paymentStatus(p.getPaymentStatus() != null ? p.getPaymentStatus().name() : null)
                .build()).toList();

        return PremiumStatementDto.builder()
                .policyId(policyId)
                .policyNumber(policy.getPolicyNumber())
                .customerName(policy.getCustomerName())
                .totalPayable(payable)
                .totalPaid(paid)
                .totalOutstanding(outstanding)
                .installments(instDtos)
                .payments(payDtos)
                .build();
    }

    @Override
    @Transactional
    public PremiumJobResultDto markOverdue(int graceDays) {
        LocalDate today = LocalDate.now();
        int grace = graceDays > 0 ? graceDays : 15;
        List<PolicyPremium> open = premiumRepository.findOutstanding(PageRequest.of(0, 1000));
        int n = 0;
        List<String> details = new ArrayList<>();
        for (PolicyPremium p : open) {
            if (p.getDueDate() == null) continue;
            if (p.getGraceEndDate() == null) {
                p.setGraceEndDate(p.getDueDate().plusDays(grace));
            }
            if (p.getDueDate().isBefore(today) && !isFullySettled(p)) {
                // keep UNPAID/PARTIAL; flag via late fee placeholder
                if (p.getLateFeeAmount() == null) p.setLateFeeAmount(BigDecimal.ZERO);
                premiumRepository.save(p);
                n++;
                details.add((p.getPolicy() != null ? p.getPolicy().getPolicyNumber() : "?") + " #" + p.getInstallmentNumber());
            }
        }
        return PremiumJobResultDto.builder().job("mark-overdue").processed(n)
                .message("Marked/refreshed grace on " + n + " installment(s).")
                .details(details.stream().limit(20).toList())
                .build();
    }

    @Override
    @Transactional
    public PremiumJobResultDto runDunning() {
        LocalDate today = LocalDate.now();
        List<PolicyPremium> overdue = premiumRepository.findDueOnOrBefore(today.minusDays(1), PageRequest.of(0, 500));
        int n = 0;
        List<String> details = new ArrayList<>();
        for (PolicyPremium p : overdue) {
            if (isFullySettled(p)) continue;
            Policy pol = p.getPolicy();
            int days = p.getDueDate() != null ? (int) ChronoUnit.DAYS.between(p.getDueDate(), today) : 0;
            int level = days <= 7 ? 1 : (days <= 15 ? 2 : 3);
            DunningNotice notice = DunningNotice.builder()
                    .policyId(pol != null ? pol.getId() : null)
                    .policyNumber(pol != null ? pol.getPolicyNumber() : null)
                    .installmentId(p.getId())
                    .dunningLevel(level)
                    .channel("SYSTEM")
                    .sentAt(LocalDateTime.now())
                    .message("Premium overdue by " + days + " day(s) — installment #" + p.getInstallmentNumber())
                    .build();
            notice.setStatus(Status.ACTIVE);
            dunningNoticeRepository.save(notice);
            n++;
            details.add(notice.getMessage());
        }
        return PremiumJobResultDto.builder().job("dunning").processed(n)
                .message("Created " + n + " dunning notice(s).")
                .details(details.stream().limit(20).toList())
                .build();
    }

    @Override
    @Transactional
    public PremiumJobResultDto runLapseCheck(int graceDays) {
        LocalDate today = LocalDate.now();
        List<PolicyPremium> open = premiumRepository.findOutstanding(PageRequest.of(0, 1000));
        Set<UUID> policyIds = new HashSet<>();
        List<String> details = new ArrayList<>();
        for (PolicyPremium p : open) {
            LocalDate graceEnd = p.getGraceEndDate() != null ? p.getGraceEndDate()
                    : (p.getDueDate() != null ? p.getDueDate().plusDays(graceDays > 0 ? graceDays : 15) : null);
            if (graceEnd != null && graceEnd.isBefore(today) && !isFullySettled(p) && p.getPolicy() != null) {
                policyIds.add(p.getPolicy().getId());
            }
        }
        int n = 0;
        for (UUID pid : policyIds) {
            Policy policy = policyRepository.findById(pid).orElse(null);
            if (policy == null || policy.getPolicyStatus() != PolicyStatus.ACTIVE) continue;
            policy.setPolicyStatus(PolicyStatus.SUSPENDED);
            policy.setCancellationReason("Auto-suspended: premium grace expired");
            policyRepository.save(policy);
            n++;
            details.add(policy.getPolicyNumber() + " suspended for non-payment");
        }
        return PremiumJobResultDto.builder().job("lapse-check").processed(n)
                .message("Suspended " + n + " policy(ies) after grace expiry.")
                .details(details)
                .build();
    }

    @Override
    @Transactional
    public ScheduleGenerateResultDto generateBackPremiumOnReinstate(UUID policyId) {
        // Create a single back-premium installment for outstanding at reinstate
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new GlobalException("POL-001", policyId));
        BigDecimal outstanding = premiumRepository.findByPolicyIdOrderByDueDateAsc(policyId).stream()
                .filter(p -> !isFullySettled(p))
                .map(this::balanceOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (outstanding.compareTo(BigDecimal.ZERO) <= 0) {
            return generateSchedule(policyId, false);
        }
        PolicyPremium back = PolicyPremium.builder()
                .policy(policy)
                .basePremium(outstanding)
                .totalPremium(outstanding)
                .payableAmount(outstanding)
                .paidAmount(BigDecimal.ZERO)
                .balanceAmount(outstanding)
                .installmentNumber(999)
                .dueDate(LocalDate.now())
                .graceEndDate(LocalDate.now().plusDays(7))
                .paymentStatus(InstallmentPaymentStatus.UNPAID.name())
                .frequency("SINGLE")
                .currencyCode(policy.getCurrencyCode())
                .build();
        back.setStatus(Status.ACTIVE);
        premiumRepository.save(back);
        return ScheduleGenerateResultDto.builder()
                .policyId(policyId)
                .policyNumber(policy.getPolicyNumber())
                .installmentCount(1)
                .totalPayable(outstanding)
                .frequency("SINGLE")
                .message("Back-premium installment created for " + outstanding)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PremiumDayEndDto dayEndSummary(UUID branchId) {
        LocalDate today = LocalDate.now();
        List<BillingPayment> todayPays = paymentRepository.findByPaymentDateBetweenAndStatus(today, today, Status.ACTIVE)
                .stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.CONFIRMED)
                .filter(p -> branchId == null || branchId.equals(p.getBranchId()))
                .toList();
        BigDecimal amount = todayPays.stream().map(BillingPayment::getAmount).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long receipts = receiptRepository.findAllByStatus(Status.ACTIVE).stream()
                .filter(r -> today.equals(r.getReceiptDate()))
                .filter(r -> branchId == null || branchId.equals(r.getBranchId()))
                .count();
        return PremiumDayEndDto.builder()
                .businessDate(today)
                .branchId(branchId)
                .collectionsCount(todayPays.size())
                .collectionsAmount(amount)
                .receiptsCount(receipts)
                .outstandingAmount(nz(premiumRepository.sumOutstanding()))
                .message("Day-end snapshot for " + today)
                .build();
    }

    @Override
    @Transactional
    public void applyEndorsementDelta(UUID policyId, BigDecimal deltaPremium, String note) {
        if (deltaPremium == null || deltaPremium.compareTo(BigDecimal.ZERO) == 0) return;
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new GlobalException("POL-001", policyId));
        if (deltaPremium.compareTo(BigDecimal.ZERO) > 0) {
            PolicyPremium extra = PolicyPremium.builder()
                    .policy(policy)
                    .basePremium(deltaPremium)
                    .totalPremium(deltaPremium)
                    .payableAmount(deltaPremium)
                    .paidAmount(BigDecimal.ZERO)
                    .balanceAmount(deltaPremium)
                    .installmentNumber(premiumRepository.findByPolicyId(policyId).size() + 1)
                    .dueDate(LocalDate.now())
                    .graceEndDate(LocalDate.now().plusDays(15))
                    .paymentStatus(InstallmentPaymentStatus.UNPAID.name())
                    .frequency("SINGLE")
                    .currencyCode(policy.getCurrencyCode())
                    .build();
            extra.setStatus(Status.ACTIVE);
            premiumRepository.save(extra);
            BigDecimal current = policy.getTotalPremium() != null ? policy.getTotalPremium() : BigDecimal.ZERO;
            policy.setTotalPremium(current.add(deltaPremium));
            policyRepository.save(policy);
        } else {
            // credit: reduce unpaid balances FIFO
            BigDecimal remaining = deltaPremium.abs();
            for (PolicyPremium p : premiumRepository.findByPolicyIdOrderByDueDateAsc(policyId)) {
                if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
                if (isFullySettled(p)) continue;
                BigDecimal bal = balanceOf(p);
                BigDecimal cut = bal.min(remaining);
                p.setPayableAmount(payableOf(p).subtract(cut));
                p.setBalanceAmount(balanceOf(p));
                if (balanceOf(p).compareTo(BigDecimal.ZERO) <= 0) {
                    p.setPaymentStatus(InstallmentPaymentStatus.CANCELLED.name());
                    p.setBalanceAmount(BigDecimal.ZERO);
                }
                premiumRepository.save(p);
                remaining = remaining.subtract(cut);
            }
            BigDecimal current = policy.getTotalPremium() != null ? policy.getTotalPremium() : BigDecimal.ZERO;
            policy.setTotalPremium(current.add(deltaPremium).max(BigDecimal.ZERO));
            policyRepository.save(policy);
        }
        log.info("Endorsement premium delta {} applied to policy {} ({})", deltaPremium, policyId, note);
    }

    @Override
    @Transactional
    public void cancelOpenInstallments(UUID policyId) {
        for (PolicyPremium p : premiumRepository.findByPolicyIdOrderByDueDateAsc(policyId)) {
            if (isFullySettled(p)) continue;
            if (nz(p.getPaidAmount()).compareTo(BigDecimal.ZERO) == 0) {
                p.setPaymentStatus(InstallmentPaymentStatus.CANCELLED.name());
                p.setBalanceAmount(BigDecimal.ZERO);
                premiumRepository.save(p);
            }
        }
    }

    // ---- helpers ----

    private PremiumCalcRule resolveRule(String productCode, String planCode) {
        if (productCode != null && planCode != null) {
            Optional<PremiumCalcRule> r = calcRuleRepository.findFirstByProductCodeAndPlanCodeAndActiveTrueAndStatus(
                    productCode, planCode, Status.ACTIVE);
            if (r.isPresent()) return r.get();
        }
        if (productCode != null) {
            return calcRuleRepository.findFirstByProductCodeAndActiveTrueAndStatus(productCode, Status.ACTIVE).orElse(null);
        }
        return null;
    }

    private String normalizeFrequency(String freq) {
        if (freq == null || freq.isBlank()) return "ANNUALLY";
        String f = freq.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        return switch (f) {
            case "MONTH", "MONTHLY", "M" -> "MONTHLY";
            case "QUARTER", "QUARTERLY", "Q" -> "QUARTERLY";
            case "SEMI", "SEMI_ANNUAL", "SEMI_ANNUALLY", "SEMIANNUALLY", "HALF_YEARLY" -> "SEMI_ANNUALLY";
            case "YEAR", "YEARLY", "ANNUAL", "ANNUALLY", "Y" -> "ANNUALLY";
            case "SINGLE", "LUMPSUM", "LUMP_SUM", "ONE_TIME" -> "SINGLE";
            default -> f;
        };
    }

    private int installmentCount(String freq, int payingTermMonthsOrYears) {
        return switch (freq) {
            case "SINGLE" -> 1;
            case "MONTHLY" -> Math.max(payingTermMonthsOrYears, 1);
            case "QUARTERLY" -> Math.max(payingTermMonthsOrYears / 3, 1);
            case "SEMI_ANNUALLY" -> Math.max(payingTermMonthsOrYears / 6, 1);
            default -> Math.max(payingTermMonthsOrYears >= 12 ? payingTermMonthsOrYears / 12 : payingTermMonthsOrYears, 1);
        };
    }

    private LocalDate dueDateFor(LocalDate start, String freq, int index) {
        return switch (freq) {
            case "MONTHLY" -> start.plusMonths(index);
            case "QUARTERLY" -> start.plusMonths(index * 3L);
            case "SEMI_ANNUALLY" -> start.plusMonths(index * 6L);
            case "ANNUALLY" -> start.plusYears(index);
            default -> start;
        };
    }

    private boolean isSettledOrPartial(PolicyPremium p) {
        String s = p.getPaymentStatus() != null ? p.getPaymentStatus().toUpperCase(Locale.ROOT) : "";
        return "PAID".equals(s) || "PARTIAL".equals(s) || "WAIVED".equals(s) || "WRITTEN_OFF".equals(s)
                || nz(p.getPaidAmount()).compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isFullySettled(PolicyPremium p) {
        String s = p.getPaymentStatus() != null ? p.getPaymentStatus().toUpperCase(Locale.ROOT) : "";
        return "PAID".equals(s) || "WAIVED".equals(s) || "WRITTEN_OFF".equals(s) || "CANCELLED".equals(s)
                || balanceOf(p).compareTo(BigDecimal.ZERO) <= 0 && nz(p.getPaidAmount()).compareTo(BigDecimal.ZERO) > 0;
    }

    private BigDecimal payableOf(PolicyPremium p) {
        return p.getPayableAmount() != null ? p.getPayableAmount() : nz(p.getTotalPremium());
    }

    private BigDecimal balanceOf(PolicyPremium p) {
        BigDecimal bal = payableOf(p).subtract(nz(p.getPaidAmount()));
        return bal.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : bal;
    }

    private BigDecimal nz(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }

    private String nextRef(String prefix) {
        return prefix + "-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
    }

    private ReasonCode parseReason(String code) {
        if (code == null) return ReasonCode.OTHER;
        try {
            return ReasonCode.valueOf(code.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return ReasonCode.OTHER;
        }
    }

    private PremiumCalcRuleDto toRuleDto(PremiumCalcRule e) {
        return PremiumCalcRuleDto.builder()
                .id(e.getId())
                .ruleCode(e.getRuleCode())
                .ruleName(e.getRuleName())
                .productCode(e.getProductCode())
                .planCode(e.getPlanCode())
                .baseRate(e.getBaseRate())
                .rateType(e.getRateType())
                .taxPercent(e.getTaxPercent())
                .loadingPercent(e.getLoadingPercent())
                .feeAmount(e.getFeeAmount())
                .defaultFrequency(e.getDefaultFrequency())
                .defaultPayingTerm(e.getDefaultPayingTerm())
                .description(e.getDescription())
                .active(e.getActive())
                .build();
    }

    private PremiumRefundDto toRefundDto(PremiumRefund e) {
        return PremiumRefundDto.builder()
                .id(e.getId())
                .refundNo(e.getRefundNo())
                .policyId(e.getPolicyId())
                .policyNumber(e.getPolicyNumber())
                .customerName(e.getCustomerName())
                .amount(e.getAmount())
                .reasonCode(e.getReasonCode() != null ? e.getReasonCode().name() : null)
                .reason(e.getReason())
                .refundDate(e.getRefundDate())
                .refundStatus(e.getRefundStatus() != null ? e.getRefundStatus().name() : null)
                .approvedBy(e.getApprovedBy())
                .build();
    }
}
