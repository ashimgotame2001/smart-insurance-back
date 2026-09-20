package com.project.smartinsurance.reportingService.service.impl;

import com.project.smartinsurance.agentService.model.AgentPerformance;
import com.project.smartinsurance.agentService.model.enums.AgentStatus;
import com.project.smartinsurance.agentService.repository.AgentPerformanceRepository;
import com.project.smartinsurance.agentService.repository.AgentRepository;
import com.project.smartinsurance.applicationConfig.model.BranchEntity;
import com.project.smartinsurance.applicationConfig.repository.BranchRepository;
import com.project.smartinsurance.billingService.model.Invoice;
import com.project.smartinsurance.billingService.model.Invoice.InvoiceStatus;
import com.project.smartinsurance.billingService.model.Receipt.ReceiptStatus;
import com.project.smartinsurance.billingService.repository.InvoiceRepository;
import com.project.smartinsurance.billingService.repository.ReceiptRepository;
import com.project.smartinsurance.claimsService.dto.ClaimDashboardSummaryDto;
import com.project.smartinsurance.claimsService.service.ClaimService;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.customerService.model.Customer;
import com.project.smartinsurance.customerService.model.CorporateCustomer;
import com.project.smartinsurance.customerService.model.GovernmentCustomer;
import com.project.smartinsurance.customerService.model.IndividualCustomer;
import com.project.smartinsurance.customerService.model.enums.CustomerType;
import com.project.smartinsurance.customerService.model.enums.KycStatus;
import com.project.smartinsurance.customerService.repository.CustomerRepository;
import com.project.smartinsurance.financeService.dto.FinancialReportDto;
import com.project.smartinsurance.financeService.service.FinancialReportService;
import com.project.smartinsurance.policyService.model.Policy;
import com.project.smartinsurance.policyService.model.enums.PolicyStatus;
import com.project.smartinsurance.policyService.repository.PolicyRepository;
import com.project.smartinsurance.reportingService.dto.DashboardResponse;
import com.project.smartinsurance.reportingService.dto.DashboardResponse.InsightItem;
import com.project.smartinsurance.reportingService.dto.DashboardResponse.KpiItem;
import com.project.smartinsurance.reportingService.dto.DashboardResponse.ProgressItem;
import com.project.smartinsurance.reportingService.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;
    private final AgentPerformanceRepository agentPerformanceRepository;
    private final BranchRepository branchRepository;
    private final InvoiceRepository invoiceRepository;
    private final ReceiptRepository receiptRepository;
    private final FinancialReportService financialReportService;
    private final ClaimService claimService;

    @Override
    public DashboardResponse getDashboard(String view, UUID branchId, UUID agentId) {
        String normalized = view == null ? "overview" : view.trim().toLowerCase(Locale.ROOT);
        UUID resolvedBranch = branchId != null ? branchId : currentBranchId();
        return switch (normalized) {
            case "executive" -> buildExecutive();
            case "branch" -> buildBranch(resolvedBranch);
            case "agent" -> buildAgent(agentId);
            case "underwriter" -> buildUnderwriter();
            case "claims" -> buildClaims();
            case "finance" -> buildFinance();
            case "sales" -> buildSales();
            case "customer" -> buildCustomer();
            case "kpi" -> buildKpi();
            default -> buildOverview();
        };
    }

    private DashboardResponse buildOverview() {
        long customers = customerRepository.countByStatus(Status.ACTIVE);
        long activePolicies = policyRepository.countByPolicyStatus(PolicyStatus.ACTIVE);
        long pendingApproval = policyRepository.countByPolicyStatus(PolicyStatus.PENDING_APPROVAL);
        BigDecimal premiumMtd = mtdPremium(null);
        long agents = agentRepository.countByDeletedFalseAndAgentStatus(AgentStatus.ACTIVE);
        long newCustomers = customerRepository.countByCreatedAtGreaterThanEqual(monthStart());
        long renewalsDue = renewalsDue(null);

        DashboardResponse res = base("overview", null);
        res.setKpis(List.of(
                kpi("customers", "Customers", customers, "number", null, null, null),
                kpi("activePolicies", "Active policies", activePolicies, "number", null, null, null),
                kpi("openClaims", "Pending review", pendingApproval, "number", null, null, "underwriting queue"),
                kpi("premiumMtd", "Premium (MTD)", premiumMtd, "money", null, null, null)
        ));
        res.setRows(recentActivity(null, 5));
        res.setInsights(List.of(
                insight("Renewals due (30d)", String.valueOf(renewalsDue), null, null),
                insight("New customers (MTD)", String.valueOf(newCustomers), null, null),
                insight("Active agents", String.valueOf(agents), null, null),
                insight("Draft policies", String.valueOf(policyRepository.countByPolicyStatus(PolicyStatus.DRAFT)), null, null)
        ));
        return res;
    }

    private DashboardResponse buildExecutive() {
        long activePolicies = policyRepository.countByPolicyStatus(PolicyStatus.ACTIVE);
        long customers = customerRepository.countByStatus(Status.ACTIVE);
        BigDecimal premium = nz(policyRepository.sumTotalPremiumByStatus(PolicyStatus.ACTIVE));
        BigDecimal claimRatio = nz(agentPerformanceRepository.avgClaimRatio());
        BigDecimal persistency = nz(agentPerformanceRepository.avgPersistencyRatio());

        DashboardResponse res = base("executive", null);
        res.setKpis(List.of(
                kpi("revenue", "Premium book", premium, "money", null, null, null),
                kpi("activePolicies", "Active policies", activePolicies, "number", null, null, null),
                kpi("customers", "Customers", customers, "number", null, null, null),
                kpi("claimsRatio", "Claims ratio", percentDisplay(claimRatio), "text", null, null, "agent avg")
        ));
        res.setRows(topBranches(5));
        res.setInsights(List.of(
                insight("Loss ratio (proxy)", percentDisplay(claimRatio), null, null),
                insight("Persistency", percentDisplay(persistency), null, null),
                insight("Combined (est.)", estimateCombined(claimRatio), null, null),
                insight("Retention proxy", percentDisplay(persistency), null, null)
        ));
        return res;
    }

    private DashboardResponse buildBranch(UUID branchId) {
        BranchEntity branch = branchId != null ? branchRepository.findById(branchId).orElse(null) : null;
        DashboardResponse res = base("branch", branch);

        if (branchId == null) {
            res.setKpis(List.of(
                    kpi("premiumMtd", "Premium (MTD)", BigDecimal.ZERO, "money", null, null, null),
                    kpi("activePolicies", "Active policies", 0, "number", null, null, null),
                    kpi("customers", "Customers", customerRepository.countByStatus(Status.ACTIVE), "number", null, null, "company-wide"),
                    kpi("pending", "Pending review", policyRepository.countByPolicyStatus(PolicyStatus.PENDING_APPROVAL), "number", null, null, null)
            ));
            res.setRows(recentActivity(null, 5));
            res.setInsights(List.of(
                    insight("Branch", "Not assigned", null, null),
                    insight("Renewals due", String.valueOf(renewalsDue(null)), null, null),
                    insight("New policies (MTD)", String.valueOf(policyRepository.countByCreatedAtGreaterThanEqual(monthStart())), null, null),
                    insight("Active agents", String.valueOf(agentRepository.countByDeletedFalseAndAgentStatus(AgentStatus.ACTIVE)), null, null)
            ));
            res.getMeta().put("note", "No branch on session — showing company-wide figures");
            return res;
        }

        long activePolicies = policyRepository.countByPolicyStatusAndBranchId(PolicyStatus.ACTIVE, branchId);
        long pending = policyRepository.countByPolicyStatusAndBranchId(PolicyStatus.PENDING_APPROVAL, branchId);
        BigDecimal premiumMtd = mtdPremium(branchId);
        long agents = agentRepository.countByDeletedFalseAndBranchId(branchId);
        long newPolicies = policyRepository.countByBranchIdAndCreatedAtGreaterThanEqual(branchId, monthStart());

        res.setKpis(List.of(
                kpi("premiumMtd", "Premium (MTD)", premiumMtd, "money", null, null, null),
                kpi("activePolicies", "Active policies", activePolicies, "number", null, null, null),
                kpi("agents", "Branch agents", agents, "number", null, null, null),
                kpi("pending", "Pending review", pending, "number", null, null, null)
        ));
        res.setRows(recentActivity(branchId, 5));
        res.setInsights(List.of(
                insight("Policies at branch", String.valueOf(policyRepository.countByBranchId(branchId)), null, null),
                insight("New policies (MTD)", String.valueOf(newPolicies), null, null),
                insight("Renewals due (30d)", String.valueOf(renewalsDue(branchId)), null, null),
                insight("Draft policies", String.valueOf(policyRepository.countByPolicyStatusAndBranchId(PolicyStatus.DRAFT, branchId)), null, null)
        ));
        return res;
    }

    private DashboardResponse buildAgent(UUID agentId) {
        DashboardResponse res = base("agent", null);
        if (agentId != null) {
            AgentPerformance perf = agentPerformanceRepository.findByAgentId(agentId).orElse(null);
            res.setKpis(List.of(
                    kpi("commission", "Commission earned", perf != null ? nz(perf.getCommissionEarned()) : BigDecimal.ZERO, "money", null, null, null),
                    kpi("policiesSold", "Policies sold", perf != null && perf.getTotalPoliciesSold() != null ? perf.getTotalPoliciesSold() : 0, "number", null, null, null),
                    kpi("premium", "Premium collected", perf != null ? nz(perf.getTotalPremiumCollected()) : BigDecimal.ZERO, "money", null, null, null),
                    kpi("persistency", "Persistency", percentDisplay(perf != null ? perf.getPersistencyRatio() : null), "text", null, null, null)
            ));
            res.getMeta().put("agentId", agentId.toString());
        } else {
            res.setKpis(List.of(
                    kpi("commission", "Commission (all)", nz(agentPerformanceRepository.sumCommissionEarned()), "money", null, null, null),
                    kpi("policiesSold", "Policies sold", Optional.ofNullable(agentPerformanceRepository.sumPoliciesSold()).orElse(0L), "number", null, null, null),
                    kpi("premium", "Premium collected", nz(agentPerformanceRepository.sumPremiumCollected()), "money", null, null, null),
                    kpi("agents", "Active agents", agentRepository.countByDeletedFalseAndAgentStatus(AgentStatus.ACTIVE), "number", null, null, null)
            ));
        }
        res.setRows(topAgents(5));
        BigDecimal persistency = nz(agentPerformanceRepository.avgPersistencyRatio());
        res.setProgress(List.of(
                progress("Persistency", toPercent(persistency), percentDisplay(persistency)),
                progress("Active agents share", agentSharePercent(), null)
        ));
        res.setInsights(List.of(
                insight("Avg claim ratio", percentDisplay(agentPerformanceRepository.avgClaimRatio()), null, null),
                insight("Avg persistency", percentDisplay(persistency), null, null)
        ));
        return res;
    }

    private DashboardResponse buildUnderwriter() {
        long pending = policyRepository.countByPolicyStatus(PolicyStatus.PENDING_APPROVAL);
        long draft = policyRepository.countByPolicyStatus(PolicyStatus.DRAFT);
        long active = policyRepository.countByPolicyStatus(PolicyStatus.ACTIVE);
        long cancelled = policyRepository.countByPolicyStatus(PolicyStatus.CANCELLED);
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        long createdToday = policyRepository.countByCreatedAtGreaterThanEqual(todayStart);

        DashboardResponse res = base("underwriter", null);
        res.setKpis(List.of(
                kpi("pending", "Pending review", pending, "number", null, null, null),
                kpi("draft", "Draft", draft, "number", null, null, null),
                kpi("createdToday", "Created today", createdToday, "number", null, null, null),
                kpi("cancelled", "Cancelled", cancelled, "number", null, null, null)
        ));
        List<Policy> queue = policyRepository.findByPolicyStatus(PolicyStatus.PENDING_APPROVAL, PageRequest.of(0, 8)).getContent();
        if (queue.isEmpty()) {
            queue = policyRepository.findByPolicyStatus(PolicyStatus.DRAFT, PageRequest.of(0, 8)).getContent();
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Policy p : queue) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", p.getPolicyNumber());
            row.put("customer", nullToDash(p.getCustomerName()));
            row.put("product", nullToDash(p.getProductCode()));
            row.put("amount", p.getTotalSumInsured() != null ? p.getTotalSumInsured() : p.getTotalPremium());
            row.put("status", p.getPolicyStatus() != null ? p.getPolicyStatus().name() : "—");
            row.put("priority", p.getPolicyStatus() == PolicyStatus.PENDING_APPROVAL ? "High" : "Normal");
            rows.add(row);
        }
        res.setRows(rows);
        long total = Math.max(active + pending + draft + cancelled, 1);
        res.setInsights(List.of(
                insight("Decision backlog", String.valueOf(pending + draft), null, null),
                insight("Active book", String.valueOf(active), null, null),
                insight("Pending share", Math.round(pending * 100.0 / total) + "%", null, null),
                insight("Cancelled", String.valueOf(cancelled), null, null)
        ));
        return res;
    }

    private DashboardResponse buildClaims() {
        ClaimDashboardSummaryDto summary = claimService.getDashboardSummary();
        DashboardResponse res = base("claims", null);
        res.setKpis(List.of(
                kpi("openClaims", "Open claims", summary.getOpenCount(), "number", null, null, null),
                kpi("pendingReview", "Pending approval", summary.getPendingApprovalCount(), "number", null, null, null),
                kpi("paidYtd", "Paid YTD", summary.getPaidYtdAmount() != null ? summary.getPaidYtdAmount() : BigDecimal.ZERO, "money", null, null, null),
                kpi("fraudOpen", "Fraud open", summary.getFraudOpenCount(), "number", null, null, null)
        ));
        res.setRows(List.of());
        res.setInsights(List.of(
                insight("Avg settlement days", String.valueOf(summary.getAvgSettlementDays() != null ? summary.getAvgSettlementDays() : 0), null, null),
                insight("Aging 0-7", String.valueOf(summary.getAging0to7()), null, null),
                insight("Aging 8-30", String.valueOf(summary.getAging8to30()), null, null),
                insight("Aging 31+", String.valueOf(summary.getAging31plus()), null, null)
        ));
        res.getMeta().put("claimsAvailable", true);
        return res;
    }

    private DashboardResponse buildFinance() {
        BigDecimal premiumBook = nz(policyRepository.sumTotalPremiumByStatus(PolicyStatus.ACTIVE));
        BigDecimal overdue = nz(invoiceRepository.sumAmountByStatusAndInvoiceStatus(Status.ACTIVE, InvoiceStatus.OVERDUE));
        BigDecimal pendingInv = nz(invoiceRepository.sumAmountByStatusAndInvoiceStatus(Status.ACTIVE, InvoiceStatus.PENDING));
        BigDecimal due = overdue.add(pendingInv);
        BigDecimal receiptsMtd = nz(receiptRepository.sumAmountBetween(Status.ACTIVE, ReceiptStatus.ISSUED, monthStartDate(), LocalDate.now()));

        BigDecimal netIncome = BigDecimal.ZERO;
        try {
            LocalDate from = monthStartDate();
            FinancialReportDto pl = financialReportService.getProfitAndLoss(from, LocalDate.now());
            if (pl != null && pl.getNetProfitLoss() != null) {
                netIncome = pl.getNetProfitLoss();
            }
        } catch (Exception ignored) {
            // Chart of accounts may be empty
        }

        DashboardResponse res = base("finance", null);
        res.setKpis(List.of(
                kpi("premium", "Premium book", premiumBook, "money", null, null, null),
                kpi("receiptsMtd", "Receipts (MTD)", receiptsMtd, "money", null, null, null),
                kpi("netIncome", "Net P&L (MTD)", netIncome, "money", null, null, null),
                kpi("due", "Due premiums", due, "money", null, null, "AR")
        ));

        List<Invoice> invoices = invoiceRepository.findByStatusAndInvoiceStatusInOrderByDueDateAsc(
                Status.ACTIVE,
                List.of(InvoiceStatus.OVERDUE, InvoiceStatus.PENDING),
                PageRequest.of(0, 8)
        );
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Invoice inv : invoices) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("inv", inv.getInvoiceNo());
            row.put("customer", inv.getCustomerName());
            row.put("amount", inv.getTotalAmount() != null ? inv.getTotalAmount() : inv.getAmount());
            row.put("due", inv.getDueDate() != null ? inv.getDueDate().toString() : "—");
            row.put("status", inv.getInvoiceStatus() != null ? inv.getInvoiceStatus().name() : "—");
            rows.add(row);
        }
        res.setRows(rows);

        BigDecimal claimRatio = nz(agentPerformanceRepository.avgClaimRatio());
        res.setInsights(List.of(
                insight("Overdue invoices", String.valueOf(invoiceRepository.countByStatusAndInvoiceStatus(Status.ACTIVE, InvoiceStatus.OVERDUE)), null, null),
                insight("Pending invoices", String.valueOf(invoiceRepository.countByStatusAndInvoiceStatus(Status.ACTIVE, InvoiceStatus.PENDING)), null, null),
                insight("Loss ratio proxy", percentDisplay(claimRatio), null, null),
                insight("Paid invoices", String.valueOf(invoiceRepository.countByStatusAndInvoiceStatus(Status.ACTIVE, InvoiceStatus.PAID)), null, null)
        ));
        return res;
    }

    private DashboardResponse buildSales() {
        BigDecimal premiumMtd = mtdPremium(null);
        long soldMtd = policyRepository.countByCreatedAtGreaterThanEqual(monthStart());
        long newCustomers = customerRepository.countByCreatedAtGreaterThanEqual(monthStart());
        long active = policyRepository.countByPolicyStatus(PolicyStatus.ACTIVE);
        long draft = policyRepository.countByPolicyStatus(PolicyStatus.DRAFT);
        long conversionBase = Math.max(active + draft + policyRepository.countByPolicyStatus(PolicyStatus.PENDING_APPROVAL), 1);
        int conversion = (int) Math.round(active * 100.0 / conversionBase);

        DashboardResponse res = base("sales", null);
        res.setKpis(List.of(
                kpi("revenue", "Premium (MTD)", premiumMtd, "money", null, null, null),
                kpi("sold", "Policies created (MTD)", soldMtd, "number", null, null, null),
                kpi("newCustomers", "New customers", newCustomers, "number", null, null, null),
                kpi("conversion", "Active share", conversion + "%", "text", null, null, null)
        ));
        res.setRows(productMix(8));
        List<ProgressItem> progress = new ArrayList<>();
        for (Map<String, Object> row : res.getRows()) {
            long sold = ((Number) row.getOrDefault("sold", 0)).longValue();
            progress.add(progress(String.valueOf(row.get("product")), (int) Math.min(100, sold * 10), sold + " sold"));
            if (progress.size() >= 4) break;
        }
        res.setProgress(progress);
        return res;
    }

    private DashboardResponse buildCustomer() {
        long total = customerRepository.countByStatus(Status.ACTIVE);
        long individual = customerRepository.countByCustomerType(CustomerType.INDIVIDUAL);
        long corporate = customerRepository.countByCustomerType(CustomerType.CORPORATE);
        long kycPending = customerRepository.countByKycStatus(KycStatus.PENDING);
        long newMtd = customerRepository.countByCreatedAtGreaterThanEqual(monthStart());

        DashboardResponse res = base("customer", null);
        res.setKpis(List.of(
                kpi("total", "Total customers", total, "number", null, null, null),
                kpi("individual", "Individual", individual, "number", null, null, null),
                kpi("corporate", "Corporate", corporate, "number", null, null, null),
                kpi("kycPending", "KYC pending", kycPending, "number", null, null, null)
        ));

        List<Map<String, Object>> rows = new ArrayList<>();
        for (Customer c : customerRepository.findRecent(PageRequest.of(0, 8))) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("name", customerDisplayName(c));
            row.put("type", c.getCustomerType() != null ? c.getCustomerType().name() : "—");
            row.put("plan", c.getCustomerCode());
            row.put("kyc", c.getKycStatus() != null ? c.getKycStatus().name() : "—");
            rows.add(row);
        }
        res.setRows(rows);
        res.setInsights(List.of(
                insight("Government", String.valueOf(customerRepository.countByCustomerType(CustomerType.GOVERNMENT)), null, null),
                insight("KYC verified", String.valueOf(customerRepository.countByKycStatus(KycStatus.VERIFIED)), null, null),
                insight("New this month", String.valueOf(newMtd), null, null),
                insight("KYC rejected", String.valueOf(customerRepository.countByKycStatus(KycStatus.REJECTED)), null, null)
        ));
        return res;
    }

    private DashboardResponse buildKpi() {
        long active = policyRepository.countByPolicyStatus(PolicyStatus.ACTIVE);
        long pending = policyRepository.countByPolicyStatus(PolicyStatus.PENDING_APPROVAL);
        long customers = customerRepository.countByStatus(Status.ACTIVE);
        long kycPending = customerRepository.countByKycStatus(KycStatus.PENDING);
        long newCustomers = customerRepository.countByCreatedAtGreaterThanEqual(monthStart());
        long soldMtd = policyRepository.countByCreatedAtGreaterThanEqual(monthStart());
        BigDecimal persistency = nz(agentPerformanceRepository.avgPersistencyRatio());

        int onTarget = 0;
        int atRisk = 0;
        List<Map<String, Object>> focus = new ArrayList<>();

        focus.add(kpiRow("Active policies", String.valueOf(active), "—", active > 0 ? "success" : "warning"));
        if (active > 0) onTarget++; else atRisk++;

        focus.add(kpiRow("Policies created (MTD)", String.valueOf(soldMtd), "—", soldMtd > 0 ? "success" : "warning"));
        if (soldMtd > 0) onTarget++; else atRisk++;

        focus.add(kpiRow("New customers (MTD)", String.valueOf(newCustomers), "—", newCustomers > 0 ? "success" : "warning"));
        if (newCustomers > 0) onTarget++; else atRisk++;

        focus.add(kpiRow("KYC completion", (customers == 0 ? 100 : Math.round((customers - kycPending) * 100.0 / Math.max(customers, 1))) + "%", "90%", kycPending == 0 ? "success" : "warning"));
        if (kycPending == 0) onTarget++; else atRisk++;

        focus.add(kpiRow("Underwriting backlog", String.valueOf(pending), "0", pending == 0 ? "success" : "warning"));
        if (pending == 0) onTarget++; else atRisk++;

        focus.add(kpiRow("Persistency", percentDisplay(persistency), "80%", toPercent(persistency) >= 80 ? "success" : "warning"));
        if (toPercent(persistency) >= 80) onTarget++; else atRisk++;

        int score = (int) Math.round(onTarget * 100.0 / Math.max(onTarget + atRisk, 1));

        DashboardResponse res = base("kpi", null);
        res.setKpis(List.of(
                kpi("score", "Overall score", score, "number", null, null, "of 100"),
                kpi("onTarget", "On target", onTarget, "number", null, null, "KPIs"),
                kpi("atRisk", "At risk", atRisk, "number", null, null, "need focus"),
                kpi("customers", "Customers", customers, "number", null, null, null)
        ));
        res.setRows(focus);
        res.setProgress(List.of(
                progress("Policies", Math.min(100, (int) Math.min(active, 100)), String.valueOf(active)),
                progress("Sales (MTD)", Math.min(100, (int) Math.min(soldMtd * 5, 100)), String.valueOf(soldMtd)),
                progress("KYC", customers == 0 ? 100 : (int) Math.round((customers - kycPending) * 100.0 / Math.max(customers, 1)), null),
                progress("Persistency", toPercent(persistency), percentDisplay(persistency))
        ));
        return res;
    }

    // ---- helpers ----

    private DashboardResponse base(String view, BranchEntity branch) {
        return DashboardResponse.builder()
                .view(view)
                .generatedAt(LocalDateTime.now())
                .branchId(branch != null ? branch.getId().toString() : null)
                .branchName(branch != null ? branch.getBranchName() : null)
                .build();
    }

    private KpiItem kpi(String key, String label, Object value, String format, String trend, Boolean up, String hint) {
        return KpiItem.builder().key(key).label(label).value(value).format(format).trend(trend).up(up).hint(hint).build();
    }

    private InsightItem insight(String label, String value, String change, Boolean positive) {
        return InsightItem.builder().label(label).value(value).change(change).positive(positive).build();
    }

    private ProgressItem progress(String label, int percent, String value) {
        return ProgressItem.builder().label(label).percent(Math.max(0, Math.min(100, percent))).value(value).build();
    }

    private Map<String, Object> kpiRow(String name, String actual, String target, String status) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("name", name);
        row.put("actual", actual);
        row.put("target", target);
        row.put("status", status);
        return row;
    }

    private BigDecimal mtdPremium(UUID branchId) {
        LocalDate from = monthStartDate();
        LocalDate to = LocalDate.now();
        if (branchId != null) {
            return nz(policyRepository.sumTotalPremiumByStatusAndBranchAndIssueDateBetween(PolicyStatus.ACTIVE, branchId, from, to));
        }
        return nz(policyRepository.sumTotalPremiumByStatusAndIssueDateBetween(PolicyStatus.ACTIVE, from, to));
    }

    private long renewalsDue(UUID branchId) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(30);
        if (branchId != null) {
            return policyRepository.countByPolicyStatusAndBranchIdAndExpiryDateBetween(PolicyStatus.ACTIVE, branchId, start, end);
        }
        return policyRepository.countByPolicyStatusAndExpiryDateBetween(PolicyStatus.ACTIVE, start, end);
    }

    private List<Map<String, Object>> topBranches(int limit) {
        List<Object[]> agg = policyRepository.aggregatePremiumByBranch(PolicyStatus.ACTIVE);
        Map<UUID, BranchEntity> branches = new HashMap<>();
        branchRepository.findAll().forEach(b -> branches.put(b.getId(), b));
        List<Map<String, Object>> rows = new ArrayList<>();
        int i = 0;
        for (Object[] row : agg) {
            if (i++ >= limit) break;
            UUID id = (UUID) row[0];
            BranchEntity b = branches.get(id);
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("branch", b != null ? b.getBranchName() : id.toString());
            m.put("policies", ((Number) row[1]).longValue());
            m.put("premium", row[2]);
            m.put("growth", "—");
            rows.add(m);
        }
        return rows;
    }

    private List<Map<String, Object>> productMix(int limit) {
        List<Object[]> agg = policyRepository.aggregateByProduct(PolicyStatus.ACTIVE);
        List<Map<String, Object>> rows = new ArrayList<>();
        int i = 0;
        for (Object[] row : agg) {
            if (i++ >= limit) break;
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("product", row[0] != null ? row[0].toString() : "UNKNOWN");
            m.put("sold", ((Number) row[1]).longValue());
            m.put("revenue", row[2]);
            m.put("growth", "—");
            rows.add(m);
        }
        return rows;
    }

    private List<Map<String, Object>> topAgents(int limit) {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (AgentPerformance p : agentPerformanceRepository.findTopByPremium().stream().limit(limit).toList()) {
            Map<String, Object> m = new LinkedHashMap<>();
            String name = p.getAgent() != null
                    ? (p.getAgent().getFullName() != null ? p.getAgent().getFullName() : p.getAgent().getAgentCode())
                    : "—";
            m.put("name", name);
            m.put("product", "Portfolio");
            m.put("stage", "Active");
            m.put("value", p.getTotalPremiumCollected());
            m.put("policies", p.getTotalPoliciesSold());
            m.put("commission", p.getCommissionEarned());
            rows.add(m);
        }
        return rows;
    }

    private List<Map<String, Object>> recentActivity(UUID branchId, int limit) {
        List<Policy> policies = branchId != null
                ? policyRepository.findRecentByBranch(branchId, PageRequest.of(0, limit))
                : policyRepository.findRecent(PageRequest.of(0, limit));
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Policy p : policies) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("time", p.getCreatedAt() != null ? p.getCreatedAt().toLocalTime().withNano(0).toString() : "—");
            m.put("item", "Policy " + p.getPolicyNumber() + (p.getCustomerName() != null ? " — " + p.getCustomerName() : ""));
            m.put("type", "Policy");
            m.put("status", p.getPolicyStatus() != null ? prettyStatus(p.getPolicyStatus().name()) : "—");
            rows.add(m);
        }
        if (rows.size() < limit && branchId == null) {
            for (Customer c : customerRepository.findRecent(PageRequest.of(0, limit - rows.size()))) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("time", c.getCreatedAt() != null ? c.getCreatedAt().toLocalTime().withNano(0).toString() : "—");
                m.put("item", "Customer " + customerDisplayName(c));
                m.put("type", "Customer");
                m.put("status", c.getKycStatus() == KycStatus.VERIFIED ? "Active" : "Pending");
                rows.add(m);
            }
        }
        return rows;
    }

    private String customerDisplayName(Customer c) {
        if (c instanceof IndividualCustomer ind) {
            String fn = ind.getFirstName() != null ? ind.getFirstName() : "";
            String ln = ind.getLastName() != null ? ind.getLastName() : "";
            String name = (fn + " " + ln).trim();
            if (!name.isEmpty()) return name;
        } else if (c instanceof CorporateCustomer corp && corp.getCompanyName() != null) {
            return corp.getCompanyName();
        } else if (c instanceof GovernmentCustomer gov && gov.getMinistryName() != null) {
            return gov.getMinistryName();
        }
        return c.getCustomerCode() != null ? c.getCustomerCode() : c.getId().toString();
    }

    private int agentSharePercent() {
        long active = agentRepository.countByDeletedFalseAndAgentStatus(AgentStatus.ACTIVE);
        long total = Math.max(agentRepository.countByDeletedFalse(), 1);
        return (int) Math.round(active * 100.0 / total);
    }

    private String estimateCombined(BigDecimal claimRatio) {
        int loss = toPercent(claimRatio);
        return (loss + 22) + "%";
    }

    private UUID currentBranchId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getDetails() instanceof Map<?, ?> details) {
                Object bid = details.get("branchId");
                if (bid != null && !bid.toString().isBlank()) {
                    return UUID.fromString(bid.toString());
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private LocalDateTime monthStart() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
    }

    private LocalDate monthStartDate() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    }

    private BigDecimal nz(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }

    private int toPercent(BigDecimal ratio) {
        if (ratio == null) return 0;
        // Stored ratios may be 0-1 or already 0-100
        BigDecimal v = ratio;
        if (v.compareTo(BigDecimal.ONE) <= 0) {
            v = v.multiply(BigDecimal.valueOf(100));
        }
        return v.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    private String percentDisplay(BigDecimal ratio) {
        return toPercent(ratio) + "%";
    }

    private String nullToDash(String v) {
        return v == null || v.isBlank() ? "—" : v;
    }

    private String prettyStatus(String raw) {
        if (raw == null) return "—";
        return switch (raw) {
            case "ACTIVE" -> "Active";
            case "PENDING_APPROVAL" -> "Pending";
            case "DRAFT" -> "New";
            case "CANCELLED" -> "Rejected";
            case "SUSPENDED" -> "Flagged";
            default -> raw.charAt(0) + raw.substring(1).toLowerCase(Locale.ROOT).replace('_', ' ');
        };
    }
}
