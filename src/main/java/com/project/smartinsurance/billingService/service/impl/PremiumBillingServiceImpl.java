package com.project.smartinsurance.billingService.service.impl;

import com.project.smartinsurance.billingService.dto.*;
import com.project.smartinsurance.billingService.mapper.BillingMapper;
import com.project.smartinsurance.billingService.model.*;
import com.project.smartinsurance.billingService.model.BillingPayment.PaymentStatus;
import com.project.smartinsurance.billingService.model.Invoice.InvoiceStatus;
import com.project.smartinsurance.billingService.model.PremiumAdjustment.AdjustmentStatus;
import com.project.smartinsurance.billingService.model.PremiumAdjustment.AdjustmentType;
import com.project.smartinsurance.billingService.model.Receipt.ReceiptStatus;
import com.project.smartinsurance.billingService.model.enums.InstallmentPaymentStatus;
import com.project.smartinsurance.billingService.repository.*;
import com.project.smartinsurance.billingService.service.PremiumBillingService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.policyService.dto.PolicyPremiumDto;
import com.project.smartinsurance.policyService.model.Policy;
import com.project.smartinsurance.policyService.model.PolicyPremium;
import com.project.smartinsurance.policyService.repository.PolicyPremiumRepository;
import com.project.smartinsurance.policyService.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PremiumBillingServiceImpl implements PremiumBillingService {

    private final PolicyPremiumRepository premiumRepository;
    private final PolicyRepository policyRepository;
    private final InvoiceRepository invoiceRepository;
    private final ReceiptRepository receiptRepository;
    private final BillingPaymentRepository paymentRepository;
    private final PaymentAllocationRepository allocationRepository;
    private final PremiumAdjustmentRepository adjustmentRepository;
    private final OnlinePaymentRepository onlinePaymentRepository;
    private final BillingMapper billingMapper;
    private final PremiumGlPostingService glPostingService;

    @Override
    @Transactional(readOnly = true)
    public BillingDeskSummaryDto getDeskSummary() {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());

        long dueToday = premiumRepository.countDueToday(today);
        long overdue = premiumRepository.countOverdue(today);
        BigDecimal outstanding = nz(premiumRepository.sumOutstanding());

        BigDecimal collectedMtd = paymentRepository.findByPaymentDateBetweenAndStatus(monthStart, today, Status.ACTIVE)
                .stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.CONFIRMED)
                .map(BillingPayment::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long receiptsMtd = receiptRepository.findAllByStatus(Status.ACTIVE).stream()
                .filter(r -> r.getReceiptDate() != null && !r.getReceiptDate().isBefore(monthStart)
                        && r.getReceiptStatus() == ReceiptStatus.ISSUED)
                .count();

        long invoicesPending = invoiceRepository.countByStatusAndInvoiceStatus(Status.ACTIVE, InvoiceStatus.PENDING)
                + invoiceRepository.countByStatusAndInvoiceStatus(Status.ACTIVE, InvoiceStatus.OVERDUE);

        BigDecimal dueTodayAmount = premiumRepository.findDueOnOrBefore(today, PageRequest.of(0, 500)).stream()
                .filter(p -> today.equals(p.getDueDate()))
                .map(this::balanceOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BillingDeskSummaryDto.builder()
                .dueTodayCount(dueToday)
                .dueTodayAmount(dueTodayAmount)
                .overdueCount(overdue)
                .outstandingAmount(outstanding)
                .collectedMtd(collectedMtd)
                .invoicesPending(invoicesPending)
                .receiptsMtd(receiptsMtd)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyPremiumDto> listSchedules(UUID policyId) {
        if (policyId == null) {
            return premiumRepository.findOutstanding(PageRequest.of(0, 100)).stream()
                    .map(this::toPremiumDto)
                    .toList();
        }
        return premiumRepository.findByPolicyIdOrderByDueDateAsc(policyId).stream()
                .map(this::toPremiumDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyPremiumDto> listInstallments(String status, int limit) {
        int size = limit > 0 ? Math.min(limit, 500) : 200;
        List<PolicyPremium> all = premiumRepository.findOutstanding(PageRequest.of(0, size));
        if (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) {
            String s = status.toUpperCase(Locale.ROOT);
            all = all.stream().filter(p -> normalizeStatus(p).equals(s)).toList();
        }
        // Also include paid recent if filtering PAID
        if (status != null && "PAID".equalsIgnoreCase(status)) {
            all = premiumRepository.findAll().stream()
                    .filter(p -> "PAID".equalsIgnoreCase(normalizeStatus(p)))
                    .sorted(Comparator.comparing(PolicyPremium::getDueDate, Comparator.nullsLast(Comparator.reverseOrder())))
                    .limit(size)
                    .toList();
        }
        return all.stream().map(this::toPremiumDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OutstandingAgingDto getOutstanding(int limit) {
        LocalDate today = LocalDate.now();
        List<PolicyPremium> rows = premiumRepository.findOutstanding(PageRequest.of(0, Math.max(limit, 50)));
        BigDecimal current = BigDecimal.ZERO, d1 = BigDecimal.ZERO, d31 = BigDecimal.ZERO, d61 = BigDecimal.ZERO, d90 = BigDecimal.ZERO;
        List<PolicyPremiumDto> dtos = new ArrayList<>();
        for (PolicyPremium p : rows) {
            BigDecimal bal = balanceOf(p);
            int days = p.getDueDate() == null ? 0 : (int) ChronoUnit.DAYS.between(p.getDueDate(), today);
            if (days <= 0) current = current.add(bal);
            else if (days <= 30) d1 = d1.add(bal);
            else if (days <= 60) d31 = d31.add(bal);
            else if (days <= 90) d61 = d61.add(bal);
            else d90 = d90.add(bal);
            PolicyPremiumDto dto = toPremiumDto(p);
            dto.setDaysOverdue(Math.max(days, 0));
            dtos.add(dto);
        }
        BigDecimal total = current.add(d1).add(d31).add(d61).add(d90);
        return OutstandingAgingDto.builder()
                .current(current).days1to30(d1).days31to60(d31).days61to90(d61).days90plus(d90).total(total)
                .rows(dtos)
                .build();
    }

    @Override
    @Transactional
    public CollectionResultDto collect(CollectionRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new GlobalException("BIL-SER-010");
        }
        Policy policy = null;
        if (request.getPolicyId() != null) {
            policy = policyRepository.findById(request.getPolicyId()).orElse(null);
        } else if (request.getPolicyNumber() != null && !request.getPolicyNumber().isBlank()) {
            policy = policyRepository.findByPolicyNumber(request.getPolicyNumber()).orElse(null);
        }

        String paymentRef = nextRef("PAY");
        String receiptNo = nextRef("RCT");
        LocalDate payDate = request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now();

        BillingPayment payment = BillingPayment.builder()
                .paymentRef(paymentRef)
                .policyId(policy != null ? policy.getId() : request.getPolicyId())
                .policyNumber(policy != null ? policy.getPolicyNumber() : request.getPolicyNumber())
                .customerId(policy != null ? policy.getCustomerId() : request.getCustomerId())
                .customerName(firstNonBlank(request.getCustomerName(), policy != null ? policy.getCustomerName() : null, "Customer"))
                .branchId(request.getBranchId() != null ? request.getBranchId() : (policy != null ? policy.getBranchId() : null))
                .amount(request.getAmount())
                .paymentMode(request.getPaymentMode() != null ? request.getPaymentMode() : "CASH")
                .paymentDate(payDate)
                .valueDate(payDate)
                .referenceNo(request.getReferenceNo())
                .description(request.getDescription())
                .collectedBy(request.getCollectedBy())
                .receiptNo(receiptNo)
                .paymentStatus(PaymentStatus.CONFIRMED)
                .build();
        payment.setStatus(Status.ACTIVE);
        payment = paymentRepository.save(payment);

        Receipt receipt = Receipt.builder()
                .receiptNo(receiptNo)
                .paymentId(payment.getId())
                .policyId(payment.getPolicyId())
                .policyNumber(payment.getPolicyNumber())
                .customerId(payment.getCustomerId())
                .branchId(payment.getBranchId())
                .customerName(payment.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .amount(request.getAmount())
                .receiptDate(payDate)
                .paymentMode(payment.getPaymentMode())
                .referenceNo(request.getReferenceNo())
                .description(request.getDescription())
                .receiptStatus(ReceiptStatus.ISSUED)
                .build();
        receipt.setStatus(Status.ACTIVE);
        receipt = receiptRepository.save(receipt);

        payment.setReceiptId(receipt.getId());
        paymentRepository.save(payment);

        List<CollectionRequest.AllocationLine> lines = request.getAllocations();
        if (lines == null || lines.isEmpty()) {
            lines = autoAllocate(payment.getPolicyId(), request.getAmount());
        }
        BigDecimal remaining = request.getAmount();
        for (CollectionRequest.AllocationLine line : lines) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
            BigDecimal alloc = line.getAmount() != null ? line.getAmount() : remaining;
            if (alloc.compareTo(remaining) > 0) alloc = remaining;
            if (alloc.compareTo(BigDecimal.ZERO) <= 0) continue;

            PaymentAllocation allocation = PaymentAllocation.builder()
                    .paymentId(payment.getId())
                    .installmentId(line.getInstallmentId())
                    .invoiceId(line.getInvoiceId())
                    .allocatedAmount(alloc)
                    .build();
            allocation.setStatus(Status.ACTIVE);
            allocationRepository.save(allocation);

            if (line.getInstallmentId() != null) {
                applyToInstallment(line.getInstallmentId(), alloc, payDate);
            }
            if (line.getInvoiceId() != null) {
                applyToInvoice(line.getInvoiceId(), alloc);
            }
            remaining = remaining.subtract(alloc);
        }

        // If still remaining and policy known, allocate FIFO to unpaid installments
        if (remaining.compareTo(BigDecimal.ZERO) > 0 && payment.getPolicyId() != null) {
            for (PolicyPremium prem : premiumRepository.findByPolicyIdOrderByDueDateAsc(payment.getPolicyId())) {
                if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
                if (isSettled(prem)) continue;
                BigDecimal bal = balanceOf(prem);
                BigDecimal alloc = bal.min(remaining);
                PaymentAllocation allocation = PaymentAllocation.builder()
                        .paymentId(payment.getId())
                        .installmentId(prem.getId())
                        .invoiceId(prem.getInvoiceId())
                        .allocatedAmount(alloc)
                        .build();
                allocation.setStatus(Status.ACTIVE);
                allocationRepository.save(allocation);
                applyToInstallment(prem.getId(), alloc, payDate);
                if (prem.getInvoiceId() != null) applyToInvoice(prem.getInvoiceId(), alloc);
                remaining = remaining.subtract(alloc);
            }
        }

        glPostingService.postPremiumCollection(payment);

        return CollectionResultDto.builder()
                .paymentId(payment.getId())
                .paymentRef(payment.getPaymentRef())
                .receiptId(receipt.getId())
                .receiptNo(receipt.getReceiptNo())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus().name())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillingPaymentDto> listPayments() {
        return paymentRepository.findAllByStatus(Status.ACTIVE).stream()
                .sorted(Comparator.comparing(BillingPayment::getPaymentDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toPaymentDto)
                .toList();
    }

    @Override
    @Transactional
    public GenerateInvoicesResultDto generateInvoices(GenerateInvoicesRequest request) {
        LocalDate asOf = request.getAsOfDate() != null ? request.getAsOfDate() : LocalDate.now();
        int limit = request.getLimit() != null ? request.getLimit() : 50;
        List<PolicyPremium> dues = premiumRepository.findDueOnOrBefore(asOf, PageRequest.of(0, limit));
        List<InvoiceDto> created = new ArrayList<>();
        for (PolicyPremium prem : dues) {
            if (prem.getInvoiceId() != null) continue;
            if (isSettled(prem)) continue;
            Policy policy = prem.getPolicy();
            if (policy == null && prem.getPolicy() == null) {
                // lazy: try via join — PolicyPremium has policy ManyToOne
            }
            Policy pol = prem.getPolicy();
            if (pol == null) continue;

            BigDecimal amount = balanceOf(prem);
            Invoice inv = Invoice.builder()
                    .invoiceNo(nextRef("INV"))
                    .policyId(pol.getId())
                    .policyNumber(pol.getPolicyNumber())
                    .customerId(pol.getCustomerId())
                    .branchId(pol.getBranchId())
                    .installmentId(prem.getId())
                    .customerName(firstNonBlank(pol.getCustomerName(), "Customer"))
                    .amount(amount)
                    .taxAmount(nz(prem.getTaxAmount()))
                    .totalAmount(amount)
                    .paidAmount(BigDecimal.ZERO)
                    .invoiceDate(LocalDate.now())
                    .dueDate(prem.getDueDate() != null ? prem.getDueDate() : LocalDate.now())
                    .description("Premium installment #" + prem.getInstallmentNumber())
                    .invoiceStatus(prem.getDueDate() != null && prem.getDueDate().isBefore(LocalDate.now())
                            ? InvoiceStatus.OVERDUE : InvoiceStatus.PENDING)
                    .build();
            inv.setStatus(Status.ACTIVE);
            inv = invoiceRepository.save(inv);
            prem.setInvoiceId(inv.getId());
            ensureBalances(prem);
            premiumRepository.save(prem);
            created.add(billingMapper.toDto(inv));
        }
        return GenerateInvoicesResultDto.builder().generated(created.size()).invoices(created).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PremiumAdjustmentDto> listAdjustments() {
        return adjustmentRepository.findAllByStatus(Status.ACTIVE).stream()
                .sorted(Comparator.comparing(PremiumAdjustment::getAdjustmentDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toAdjustmentDto)
                .toList();
    }

    @Override
    @Transactional
    public PremiumAdjustmentDto createAdjustment(PremiumAdjustmentDto dto) {
        PremiumAdjustment entity = PremiumAdjustment.builder()
                .adjustmentNo(nextRef("ADJ"))
                .policyId(dto.getPolicyId())
                .policyNumber(dto.getPolicyNumber())
                .installmentId(dto.getInstallmentId())
                .customerName(dto.getCustomerName())
                .adjustmentType(parseAdjType(dto.getAdjustmentType()))
                .amount(dto.getAmount() != null ? dto.getAmount() : BigDecimal.ZERO)
                .reason(dto.getReason())
                .adjustmentDate(dto.getAdjustmentDate() != null ? dto.getAdjustmentDate() : LocalDate.now())
                .adjustmentStatus(AdjustmentStatus.PENDING)
                .build();
        entity.setStatus(Status.ACTIVE);
        return toAdjustmentDto(adjustmentRepository.save(entity));
    }

    @Override
    @Transactional
    public PremiumAdjustmentDto approveAdjustment(UUID id) {
        PremiumAdjustment adj = adjustmentRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-011"));
        adj.setAdjustmentStatus(AdjustmentStatus.APPROVED);
        if (adj.getInstallmentId() != null
                && (adj.getAdjustmentType() == AdjustmentType.WAIVER || adj.getAdjustmentType() == AdjustmentType.WRITE_OFF)) {
            PolicyPremium prem = premiumRepository.findById(adj.getInstallmentId()).orElse(null);
            if (prem != null) {
                BigDecimal waiver = nz(adj.getAmount());
                BigDecimal paid = nz(prem.getPaidAmount()).add(waiver);
                prem.setPaidAmount(paid);
                ensureBalances(prem);
                BigDecimal bal = balanceOf(prem);
                if (bal.compareTo(BigDecimal.ZERO) <= 0) {
                    prem.setPaymentStatus(adj.getAdjustmentType() == AdjustmentType.WRITE_OFF
                            ? InstallmentPaymentStatus.WRITTEN_OFF.name()
                            : InstallmentPaymentStatus.WAIVED.name());
                    prem.setPaidDate(LocalDate.now());
                    prem.setBalanceAmount(BigDecimal.ZERO);
                } else {
                    prem.setPaymentStatus(InstallmentPaymentStatus.PARTIAL.name());
                    prem.setBalanceAmount(bal);
                }
                premiumRepository.save(prem);
                adj.setAdjustmentStatus(AdjustmentStatus.APPLIED);
            }
        }
        return toAdjustmentDto(adjustmentRepository.save(adj));
    }

    @Override
    @Transactional
    public PremiumAdjustmentDto rejectAdjustment(UUID id) {
        PremiumAdjustment adj = adjustmentRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-011"));
        adj.setAdjustmentStatus(AdjustmentStatus.REJECTED);
        return toAdjustmentDto(adjustmentRepository.save(adj));
    }

    @Override
    @Transactional(readOnly = true)
    public BillingReconcileDto getReconcileSnapshot() {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
        BigDecimal collected = paymentRepository.findByPaymentDateBetweenAndStatus(monthStart, today, Status.ACTIVE)
                .stream().filter(p -> p.getPaymentStatus() == PaymentStatus.CONFIRMED)
                .map(BillingPayment::getAmount).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal receipts = receiptRepository.findAllByStatus(Status.ACTIVE).stream()
                .filter(r -> r.getReceiptDate() != null && !r.getReceiptDate().isBefore(monthStart)
                        && r.getReceiptStatus() == ReceiptStatus.ISSUED)
                .map(Receipt::getAmount).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long pendingOnline = onlinePaymentRepository.findAllByStatus(Status.ACTIVE).stream()
                .filter(o -> o.getPaymentStatus() == OnlinePayment.OnlinePaymentStatus.PENDING)
                .count();
        List<String> notes = new ArrayList<>();
        if (collected.subtract(receipts).abs().compareTo(new BigDecimal("0.01")) > 0) {
            notes.add("Collected payments and issued receipts differ for MTD — review allocations.");
        }
        if (pendingOnline > 0) {
            notes.add(pendingOnline + " online payment(s) still pending confirmation.");
        }
        if (notes.isEmpty()) {
            notes.add("No material mismatches detected for the current month.");
        }
        return BillingReconcileDto.builder()
                .billingCollectedMtd(collected)
                .receiptsIssuedMtd(receipts)
                .gatewayPending(BigDecimal.valueOf(pendingOnline))
                .unmatchedOnlineCount(pendingOnline)
                .notes(notes)
                .build();
    }

    // ---- helpers ----

    private void applyToInstallment(UUID installmentId, BigDecimal amount, LocalDate payDate) {
        PolicyPremium prem = premiumRepository.findById(installmentId)
                .orElseThrow(() -> new GlobalException("BIL-SER-012"));
        ensureBalances(prem);
        BigDecimal paid = nz(prem.getPaidAmount()).add(amount);
        prem.setPaidAmount(paid);
        BigDecimal bal = balanceOf(prem);
        if (bal.compareTo(BigDecimal.ZERO) <= 0) {
            prem.setPaymentStatus(InstallmentPaymentStatus.PAID.name());
            prem.setPaidDate(payDate);
            prem.setBalanceAmount(BigDecimal.ZERO);
        } else {
            prem.setPaymentStatus(InstallmentPaymentStatus.PARTIAL.name());
            prem.setBalanceAmount(bal);
        }
        premiumRepository.save(prem);
    }

    private void applyToInvoice(UUID invoiceId, BigDecimal amount) {
        Invoice inv = invoiceRepository.findById(invoiceId).orElse(null);
        if (inv == null) return;
        BigDecimal paid = nz(inv.getPaidAmount()).add(amount);
        inv.setPaidAmount(paid);
        BigDecimal total = nz(inv.getTotalAmount());
        if (paid.compareTo(total) >= 0) {
            inv.setInvoiceStatus(InvoiceStatus.PAID);
        } else if (paid.compareTo(BigDecimal.ZERO) > 0) {
            inv.setInvoiceStatus(InvoiceStatus.PARTIALLY_PAID);
        }
        invoiceRepository.save(inv);
    }

    private List<CollectionRequest.AllocationLine> autoAllocate(UUID policyId, BigDecimal amount) {
        List<CollectionRequest.AllocationLine> lines = new ArrayList<>();
        if (policyId == null) return lines;
        BigDecimal remaining = amount;
        for (PolicyPremium prem : premiumRepository.findByPolicyIdOrderByDueDateAsc(policyId)) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
            if (isSettled(prem)) continue;
            BigDecimal bal = balanceOf(prem);
            BigDecimal alloc = bal.min(remaining);
            lines.add(CollectionRequest.AllocationLine.builder()
                    .installmentId(prem.getId())
                    .invoiceId(prem.getInvoiceId())
                    .amount(alloc)
                    .build());
            remaining = remaining.subtract(alloc);
        }
        return lines;
    }

    private PolicyPremiumDto toPremiumDto(PolicyPremium p) {
        ensureBalances(p);
        Policy pol = p.getPolicy();
        return PolicyPremiumDto.builder()
                .id(p.getId())
                .policyId(pol != null ? pol.getId() : null)
                .policyNumber(pol != null ? pol.getPolicyNumber() : null)
                .customerName(pol != null ? pol.getCustomerName() : null)
                .basePremium(p.getBasePremium())
                .discountAmount(p.getDiscountAmount())
                .taxAmount(p.getTaxAmount())
                .surchargeAmount(p.getSurchargeAmount())
                .feeAmount(p.getFeeAmount())
                .totalPremium(p.getTotalPremium())
                .payableAmount(p.getPayableAmount())
                .paidAmount(nz(p.getPaidAmount()))
                .balanceAmount(balanceOf(p))
                .installmentNumber(p.getInstallmentNumber())
                .dueDate(p.getDueDate())
                .paidDate(p.getPaidDate())
                .paymentStatus(normalizeStatus(p))
                .invoiceId(p.getInvoiceId())
                .build();
    }

    private BillingPaymentDto toPaymentDto(BillingPayment p) {
        return BillingPaymentDto.builder()
                .id(p.getId())
                .paymentRef(p.getPaymentRef())
                .policyId(p.getPolicyId())
                .policyNumber(p.getPolicyNumber())
                .customerId(p.getCustomerId())
                .customerName(p.getCustomerName())
                .branchId(p.getBranchId())
                .amount(p.getAmount())
                .paymentMode(p.getPaymentMode())
                .paymentDate(p.getPaymentDate())
                .valueDate(p.getValueDate())
                .referenceNo(p.getReferenceNo())
                .description(p.getDescription())
                .collectedBy(p.getCollectedBy())
                .receiptId(p.getReceiptId())
                .receiptNo(p.getReceiptNo())
                .paymentStatus(p.getPaymentStatus() != null ? p.getPaymentStatus().name() : null)
                .build();
    }

    private PremiumAdjustmentDto toAdjustmentDto(PremiumAdjustment a) {
        return PremiumAdjustmentDto.builder()
                .id(a.getId())
                .adjustmentNo(a.getAdjustmentNo())
                .policyId(a.getPolicyId())
                .policyNumber(a.getPolicyNumber())
                .installmentId(a.getInstallmentId())
                .customerName(a.getCustomerName())
                .adjustmentType(a.getAdjustmentType() != null ? a.getAdjustmentType().name() : null)
                .amount(a.getAmount())
                .reason(a.getReason())
                .adjustmentDate(a.getAdjustmentDate())
                .adjustmentStatus(a.getAdjustmentStatus() != null ? a.getAdjustmentStatus().name() : null)
                .build();
    }

    private void ensureBalances(PolicyPremium p) {
        if (p.getPayableAmount() == null) {
            p.setPayableAmount(nz(p.getTotalPremium()));
        }
        if (p.getPaidAmount() == null) {
            p.setPaidAmount(BigDecimal.ZERO);
        }
        if (p.getBalanceAmount() == null) {
            p.setBalanceAmount(balanceOf(p));
        }
        if (p.getPaymentStatus() == null || p.getPaymentStatus().isBlank()) {
            p.setPaymentStatus(InstallmentPaymentStatus.UNPAID.name());
        }
    }

    private BigDecimal balanceOf(PolicyPremium p) {
        BigDecimal payable = p.getPayableAmount() != null ? p.getPayableAmount() : nz(p.getTotalPremium());
        BigDecimal paid = nz(p.getPaidAmount());
        BigDecimal bal = payable.subtract(paid);
        return bal.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : bal.setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isSettled(PolicyPremium p) {
        String s = normalizeStatus(p);
        return InstallmentPaymentStatus.PAID.name().equals(s)
                || InstallmentPaymentStatus.WAIVED.name().equals(s)
                || InstallmentPaymentStatus.WRITTEN_OFF.name().equals(s)
                || InstallmentPaymentStatus.CANCELLED.name().equals(s)
                || balanceOf(p).compareTo(BigDecimal.ZERO) <= 0 && nz(p.getPaidAmount()).compareTo(BigDecimal.ZERO) > 0;
    }

    private String normalizeStatus(PolicyPremium p) {
        if (p.getPaymentStatus() == null || p.getPaymentStatus().isBlank()) {
            return balanceOf(p).compareTo(BigDecimal.ZERO) <= 0 && nz(p.getPaidAmount()).compareTo(BigDecimal.ZERO) > 0
                    ? InstallmentPaymentStatus.PAID.name()
                    : InstallmentPaymentStatus.UNPAID.name();
        }
        String raw = p.getPaymentStatus().trim().toUpperCase(Locale.ROOT);
        return switch (raw) {
            case "PENDING", "DUE", "OPEN" -> InstallmentPaymentStatus.UNPAID.name();
            case "COMPLETE", "COMPLETED", "SETTLED" -> InstallmentPaymentStatus.PAID.name();
            default -> raw;
        };
    }

    private AdjustmentType parseAdjType(String type) {
        if (type == null) return AdjustmentType.ADJUSTMENT;
        try {
            return AdjustmentType.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return AdjustmentType.ADJUSTMENT;
        }
    }

    private String nextRef(String prefix) {
        return prefix + "-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
    }

    private BigDecimal nz(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }

    private String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }
}
