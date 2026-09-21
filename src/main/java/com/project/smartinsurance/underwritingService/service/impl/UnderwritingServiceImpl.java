package com.project.smartinsurance.underwritingService.service.impl;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.utils.CheckerMakerValidator;
import com.project.smartinsurance.policyService.model.Policy;
import com.project.smartinsurance.policyService.model.enums.PolicyStatus;
import com.project.smartinsurance.policyService.repository.PolicyRepository;
import com.project.smartinsurance.productService.model.Product;
import com.project.smartinsurance.productService.repository.EligibilityRuleRepository;
import com.project.smartinsurance.productService.repository.ProductRepository;
import com.project.smartinsurance.underwritingService.dto.*;
import com.project.smartinsurance.underwritingService.model.*;
import com.project.smartinsurance.underwritingService.model.enums.*;
import com.project.smartinsurance.underwritingService.repository.*;
import com.project.smartinsurance.underwritingService.service.UnderwritingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnderwritingServiceImpl implements UnderwritingService {

    private final UnderwritingCaseRepository caseRepository;
    private final UnderwritingDecisionRepository decisionRepository;
    private final UwApprovalStepRepository approvalStepRepository;
    private final UwAssessmentRepository assessmentRepository;
    private final PolicyRepository policyRepository;
    private final ProductRepository productRepository;
    private final EligibilityRuleRepository eligibilityRuleRepository;
    private final CheckerMakerValidator checkerMakerValidator;
    private final com.project.smartinsurance.billingService.service.PremiumLifecycleService premiumLifecycleService;

    @Override
    @Transactional
    public UnderwritingCaseDto submitForUnderwriting(SubmitForUnderwritingRequest request) {
        if (request.getPolicyId() == null) {
            throw new GlobalException("UW-001");
        }
        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new GlobalException("POL-001", request.getPolicyId()));
        if (policy.getPolicyStatus() != PolicyStatus.DRAFT && policy.getPolicyStatus() != PolicyStatus.PENDING_APPROVAL) {
            throw new GlobalException("UW-002", policy.getPolicyStatus());
        }

        caseRepository.findByPolicyId(policy.getId()).ifPresent(existing -> {
            throw new GlobalException("UW-003", existing.getCaseNumber());
        });

        Product product = productRepository.findByCode(policy.getProductCode()).orElse(null);
        StringBuilder referral = new StringBuilder();
        if (StringUtils.hasText(request.getReferralReason())) {
            referral.append(request.getReferralReason());
        }
        if (product != null && Boolean.TRUE.equals(product.getManualUnderwritingRequired())) {
            if (!referral.isEmpty()) referral.append("; ");
            referral.append("Product requires manual underwriting");
        }
        if (product != null) {
            long ruleCount = eligibilityRuleRepository.findByProductId(product.getId()).size();
            if (ruleCount > 0) {
                if (!referral.isEmpty()) referral.append("; ");
                referral.append(ruleCount).append(" eligibility rule(s) on product");
            }
            if (policy.getTotalSumInsured() != null) {
                if (product.getMaxSumAssured() != null && policy.getTotalSumInsured().compareTo(product.getMaxSumAssured()) > 0) {
                    if (!referral.isEmpty()) referral.append("; ");
                    referral.append("Sum insured exceeds product max");
                }
                if (product.getMinSumAssured() != null && policy.getTotalSumInsured().compareTo(product.getMinSumAssured()) < 0) {
                    if (!referral.isEmpty()) referral.append("; ");
                    referral.append("Sum insured below product min");
                }
            }
        }

        String maker = checkerMakerValidator.requireCurrentUsername();
        UwPriority priority = UwPriority.NORMAL;
        if (StringUtils.hasText(request.getPriority())) {
            try {
                priority = UwPriority.valueOf(request.getPriority().trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                priority = UwPriority.NORMAL;
            }
        }

        UnderwritingCase uwCase = UnderwritingCase.builder()
                .caseNumber(generateCaseNumber())
                .policyId(policy.getId())
                .policyNumber(policy.getPolicyNumber())
                .customerId(policy.getCustomerId())
                .customerName(policy.getCustomerName())
                .productCode(policy.getProductCode())
                .productName(product != null ? product.getName() : null)
                .lineOfBusiness(product != null && product.getLineOfBusiness() != null ? product.getLineOfBusiness().name() : null)
                .caseStatus(UwCaseStatus.IN_QUEUE)
                .priority(priority)
                .referralReason(referral.isEmpty() ? "Submitted for underwriting" : referral.toString())
                .referralRules(referral.toString())
                .submittedBy(maker)
                .submittedAt(LocalDateTime.now())
                .dueAt(LocalDateTime.now().plusDays(3))
                .build();

        uwCase = caseRepository.save(uwCase);

        policy.setPolicyStatus(PolicyStatus.PENDING_APPROVAL);
        policy.setApprovalStatus("PENDING");
        policyRepository.save(policy);

        computeRiskScoreInternal(uwCase);
        log.info("UW case {} created for policy {}", uwCase.getCaseNumber(), policy.getPolicyNumber());
        return toCaseDto(uwCase, true);
    }

    @Override
    @Transactional(readOnly = true)
    public UnderwritingCaseDto getCase(UUID id) {
        return toCaseDto(findCase(id), true);
    }

    @Override
    @Transactional(readOnly = true)
    public UnderwritingCaseDto getCaseByPolicy(UUID policyId) {
        UnderwritingCase uwCase = caseRepository.findByPolicyId(policyId)
                .orElseThrow(() -> new GlobalException("UW-004", policyId));
        return toCaseDto(uwCase, true);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<UnderwritingCaseDto> searchCases(String status, String assignee, String lob, String search, int page, int size) {
        UwCaseStatus st = null;
        if (StringUtils.hasText(status)) {
            try {
                st = UwCaseStatus.valueOf(status.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new GlobalException("UW-005", status);
            }
        }
        Pageable pageable = PageRequest.of(Math.max(page, 0), size <= 0 ? 10 : Math.min(size, 100),
                Sort.by("createdAt").descending());
        Page<UnderwritingCase> result = caseRepository.search(
                st,
                StringUtils.hasText(assignee) ? assignee.trim() : null,
                StringUtils.hasText(lob) ? lob.trim() : null,
                StringUtils.hasText(search) ? search.trim() : null,
                pageable);
        List<UnderwritingCaseDto> content = result.getContent().stream()
                .map(c -> toCaseDto(c, false))
                .collect(Collectors.toList());
        return PagedData.<UnderwritingCaseDto>builder()
                .content(content)
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UwQueueSummaryDto queueSummary() {
        return UwQueueSummaryDto.builder()
                .inQueue(caseRepository.countByCaseStatus(UwCaseStatus.IN_QUEUE))
                .inAssessment(caseRepository.countByCaseStatus(UwCaseStatus.IN_ASSESSMENT))
                .pendingDecision(caseRepository.countByCaseStatus(UwCaseStatus.PENDING_DECISION))
                .pendingApproval(caseRepository.countByCaseStatus(UwCaseStatus.PENDING_APPROVAL))
                .approved(caseRepository.countByCaseStatus(UwCaseStatus.APPROVED))
                .declined(caseRepository.countByCaseStatus(UwCaseStatus.DECLINED))
                .infoRequested(caseRepository.countByCaseStatus(UwCaseStatus.INFO_REQUESTED))
                .build();
    }

    @Override
    @Transactional
    public UnderwritingCaseDto assign(UUID id, UwAssignRequest request) {
        UnderwritingCase uwCase = findCase(id);
        if (StringUtils.hasText(request.getAssignee())) {
            uwCase.setAssignee(request.getAssignee().trim());
        }
        if (StringUtils.hasText(request.getPriority())) {
            try {
                uwCase.setPriority(UwPriority.valueOf(request.getPriority().trim().toUpperCase()));
            } catch (IllegalArgumentException ignored) {
                // keep existing
            }
        }
        if (StringUtils.hasText(request.getNotes())) {
            uwCase.setNotes(request.getNotes());
        }
        if (uwCase.getCaseStatus() == UwCaseStatus.IN_QUEUE) {
            uwCase.setCaseStatus(UwCaseStatus.IN_ASSESSMENT);
        }
        return toCaseDto(caseRepository.save(uwCase), true);
    }

    @Override
    @Transactional
    public UnderwritingDecisionDto recordDecision(UUID caseId, UwDecisionRequest request) {
        UnderwritingCase uwCase = findCase(caseId);
        if (uwCase.getCaseStatus() == UwCaseStatus.APPROVED || uwCase.getCaseStatus() == UwCaseStatus.DECLINED
                || uwCase.getCaseStatus() == UwCaseStatus.CLOSED) {
            throw new GlobalException("UW-006", uwCase.getCaseStatus());
        }
        UwDecisionOutcome outcome;
        try {
            outcome = UwDecisionOutcome.valueOf(request.getOutcome().trim().toUpperCase());
        } catch (Exception e) {
            throw new GlobalException("UW-007", request.getOutcome());
        }

        String actor = checkerMakerValidator.requireCurrentUsername();
        UnderwritingDecision decision = UnderwritingDecision.builder()
                .caseId(caseId)
                .outcome(outcome)
                .conditions(request.getConditions())
                .exclusions(request.getExclusions())
                .loadingPercent(request.getLoadingPercent())
                .loadingAmount(request.getLoadingAmount())
                .rationale(request.getRationale())
                .decidedBy(actor)
                .decidedAt(LocalDateTime.now())
                .finalDecision(false)
                .build();
        decision = decisionRepository.save(decision);

        if (outcome == UwDecisionOutcome.REFER_INFO || outcome == UwDecisionOutcome.POSTPONE) {
            uwCase.setCaseStatus(UwCaseStatus.INFO_REQUESTED);
            caseRepository.save(uwCase);
            return toDecisionDto(decision);
        }

        if (outcome == UwDecisionOutcome.DECLINE) {
            decision.setFinalDecision(true);
            decisionRepository.save(decision);
            uwCase.setCaseStatus(UwCaseStatus.DECLINED);
            uwCase.setClosedAt(LocalDateTime.now());
            caseRepository.save(uwCase);
            declinePolicy(uwCase.getPolicyId(), actor);
            return toDecisionDto(decision);
        }

        // ACCEPT / ACCEPT_WITH_CONDITIONS → start approval workflow
        uwCase.setCaseStatus(UwCaseStatus.PENDING_APPROVAL);
        caseRepository.save(uwCase);
        createApprovalSteps(uwCase, decision);
        return toDecisionDto(decision);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnderwritingDecisionDto> listDecisions(UUID caseId) {
        findCase(caseId);
        return decisionRepository.findByCaseIdOrderByCreatedAtDesc(caseId).stream()
                .map(this::toDecisionDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UwApprovalStepDto approveStep(UUID caseId, UUID stepId, UwApprovalActionRequest request) {
        UnderwritingCase uwCase = findCase(caseId);
        UwApprovalStep step = approvalStepRepository.findById(stepId)
                .orElseThrow(() -> new GlobalException("UW-008", stepId));
        if (!step.getCaseId().equals(caseId)) {
            throw new GlobalException("UW-008", stepId);
        }
        if (step.getStepStatus() != UwApprovalStepStatus.PENDING) {
            throw new GlobalException("UW-009", step.getStepStatus());
        }

        String checker = checkerMakerValidator.requireCurrentUsername();
        UnderwritingDecision decision = decisionRepository.findById(step.getDecisionId())
                .orElseThrow(() -> new GlobalException("UW-010", step.getDecisionId()));
        checkerMakerValidator.assertDifferentActor(decision.getDecidedBy(), checker, "UW-011");

        step.setStepStatus(UwApprovalStepStatus.APPROVED);
        step.setActedBy(checker);
        step.setActedAt(LocalDateTime.now());
        step.setComments(request != null ? request.getComments() : null);
        approvalStepRepository.save(step);

        boolean allDone = approvalStepRepository.findByCaseIdOrderByStepOrderAsc(caseId).stream()
                .allMatch(s -> s.getStepStatus() == UwApprovalStepStatus.APPROVED
                        || s.getStepStatus() == UwApprovalStepStatus.SKIPPED);
        if (allDone) {
            finalizeAcceptance(uwCase, decision, checker);
        }
        return toApprovalDto(step);
    }

    @Override
    @Transactional
    public UwApprovalStepDto rejectStep(UUID caseId, UUID stepId, UwApprovalActionRequest request) {
        findCase(caseId);
        UwApprovalStep step = approvalStepRepository.findById(stepId)
                .orElseThrow(() -> new GlobalException("UW-008", stepId));
        if (!step.getCaseId().equals(caseId)) {
            throw new GlobalException("UW-008", stepId);
        }
        String actor = checkerMakerValidator.requireCurrentUsername();
        UnderwritingDecision decision = decisionRepository.findById(step.getDecisionId())
                .orElseThrow(() -> new GlobalException("UW-010", step.getDecisionId()));
        checkerMakerValidator.assertDifferentActor(decision.getDecidedBy(), actor, "UW-011");

        step.setStepStatus(UwApprovalStepStatus.REJECTED);
        step.setActedBy(actor);
        step.setActedAt(LocalDateTime.now());
        step.setComments(request != null ? request.getComments() : null);
        approvalStepRepository.save(step);

        UnderwritingCase uwCase = findCase(caseId);
        uwCase.setCaseStatus(UwCaseStatus.PENDING_DECISION);
        caseRepository.save(uwCase);
        return toApprovalDto(step);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UwApprovalStepDto> listApprovals(UUID caseId) {
        findCase(caseId);
        return approvalStepRepository.findByCaseIdOrderByStepOrderAsc(caseId).stream()
                .map(this::toApprovalDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UwAssessmentDto createAssessment(UUID caseId, UwAssessmentRequest request) {
        UnderwritingCase uwCase = findCase(caseId);
        UwAssessmentType type = parseAssessmentType(request.getAssessmentType());
        String actor = checkerMakerValidator.requireCurrentUsername();
        UwAssessment assessment = mapAssessmentRequest(new UwAssessment(), request, type, caseId, actor);
        assessment = assessmentRepository.save(assessment);
        if (uwCase.getCaseStatus() == UwCaseStatus.IN_QUEUE || uwCase.getCaseStatus() == UwCaseStatus.INFO_REQUESTED) {
            uwCase.setCaseStatus(UwCaseStatus.IN_ASSESSMENT);
            caseRepository.save(uwCase);
        }
        if (type == UwAssessmentType.RISK && Boolean.TRUE.equals(assessment.getCompleted())) {
            uwCase.setCaseStatus(UwCaseStatus.PENDING_DECISION);
            caseRepository.save(uwCase);
        }
        return toAssessmentDto(assessment);
    }

    @Override
    @Transactional
    public UwAssessmentDto updateAssessment(UUID assessmentId, UwAssessmentRequest request) {
        UwAssessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new GlobalException("UW-012", assessmentId));
        UwAssessmentType type = request.getAssessmentType() != null
                ? parseAssessmentType(request.getAssessmentType())
                : assessment.getAssessmentType();
        String actor = checkerMakerValidator.requireCurrentUsername();
        assessment = mapAssessmentRequest(assessment, request, type, assessment.getCaseId(), actor);
        return toAssessmentDto(assessmentRepository.save(assessment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UwAssessmentDto> listAssessments(UUID caseId, String type) {
        findCase(caseId);
        if (StringUtils.hasText(type)) {
            UwAssessmentType t = parseAssessmentType(type);
            return assessmentRepository.findByCaseIdAndAssessmentTypeOrderByCreatedAtDesc(caseId, t).stream()
                    .map(this::toAssessmentDto)
                    .collect(Collectors.toList());
        }
        return assessmentRepository.findByCaseIdOrderByCreatedAtDesc(caseId).stream()
                .map(this::toAssessmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<UwAssessmentDto> listAssessmentsByType(String type, int page, int size) {
        UwAssessmentType t = parseAssessmentType(type);
        Pageable pageable = PageRequest.of(Math.max(page, 0), size <= 0 ? 10 : Math.min(size, 100),
                Sort.by("createdAt").descending());
        Page<UwAssessment> result = assessmentRepository.findByAssessmentTypeOrderByCreatedAtDesc(t, pageable);
        return PagedData.<UwAssessmentDto>builder()
                .content(result.getContent().stream().map(this::toAssessmentDto).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public UwAssessmentDto computeRiskScore(UUID caseId) {
        UnderwritingCase uwCase = findCase(caseId);
        return toAssessmentDto(computeRiskScoreInternal(uwCase));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUnderwritingCleared(UUID policyId) {
        return caseRepository.findByPolicyId(policyId)
                .map(c -> c.getCaseStatus() == UwCaseStatus.APPROVED)
                .orElse(true);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean requiresUnderwriting(String productCode) {
        if (!StringUtils.hasText(productCode)) return false;
        return productRepository.findByCode(productCode)
                .map(p -> Boolean.TRUE.equals(p.getManualUnderwritingRequired()))
                .orElse(false);
    }

    private void finalizeAcceptance(UnderwritingCase uwCase, UnderwritingDecision decision, String checker) {
        decision.setFinalDecision(true);
        decision.setApprovedBy(checker);
        decision.setApprovedAt(LocalDateTime.now());
        decisionRepository.save(decision);

        uwCase.setCaseStatus(UwCaseStatus.APPROVED);
        uwCase.setClosedAt(LocalDateTime.now());
        caseRepository.save(uwCase);

        Policy policy = policyRepository.findById(uwCase.getPolicyId())
                .orElseThrow(() -> new GlobalException("POL-001", uwCase.getPolicyId()));
        policy.setPolicyStatus(PolicyStatus.ACTIVE);
        policy.setApprovedBy(checker);
        policy.setApprovalDate(LocalDateTime.now());
        policy.setApprovalStatus("APPROVED");
        if (policy.getIssueDate() == null) {
            policy.setIssueDate(java.time.LocalDate.now());
        }
        policyRepository.save(policy);
        try {
            premiumLifecycleService.generateSchedule(policy.getId(), false);
        } catch (Exception e) {
            log.warn("Premium schedule on UW release failed for {}: {}", policy.getId(), e.getMessage());
        }
        log.info("UW case {} approved; policy {} activated", uwCase.getCaseNumber(), policy.getPolicyNumber());
    }

    private void declinePolicy(UUID policyId, String actor) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new GlobalException("POL-001", policyId));
        policy.setPolicyStatus(PolicyStatus.DECLINED);
        policy.setApprovalStatus("DECLINED");
        policy.setApprovedBy(actor);
        policy.setApprovalDate(LocalDateTime.now());
        policyRepository.save(policy);
    }

    private void createApprovalSteps(UnderwritingCase uwCase, UnderwritingDecision decision) {
        List<UwApprovalStep> existing = approvalStepRepository.findByCaseIdOrderByStepOrderAsc(uwCase.getId());
        if (!existing.isEmpty()) {
            existing.forEach(s -> {
                if (s.getStepStatus() == UwApprovalStepStatus.PENDING) {
                    s.setStepStatus(UwApprovalStepStatus.SKIPPED);
                    approvalStepRepository.save(s);
                }
            });
        }
        List<UwApprovalStep> steps = new ArrayList<>();
        steps.add(UwApprovalStep.builder()
                .caseId(uwCase.getId())
                .decisionId(decision.getId())
                .stepOrder(1)
                .stepName("Senior Underwriter")
                .approverRole("SENIOR_UW")
                .stepStatus(UwApprovalStepStatus.PENDING)
                .build());
        if (uwCase.getPriority() == UwPriority.HIGH || uwCase.getPriority() == UwPriority.URGENT
                || decision.getOutcome() == UwDecisionOutcome.ACCEPT_WITH_CONDITIONS) {
            steps.add(UwApprovalStep.builder()
                    .caseId(uwCase.getId())
                    .decisionId(decision.getId())
                    .stepOrder(2)
                    .stepName("Chief Underwriter")
                    .approverRole("CHIEF_UW")
                    .stepStatus(UwApprovalStepStatus.PENDING)
                    .build());
        }
        approvalStepRepository.saveAll(steps);
    }

    private UwAssessment computeRiskScoreInternal(UnderwritingCase uwCase) {
        Policy policy = policyRepository.findById(uwCase.getPolicyId()).orElse(null);
        Product product = productRepository.findByCode(uwCase.getProductCode()).orElse(null);
        BigDecimal score = BigDecimal.valueOf(40);
        StringBuilder drivers = new StringBuilder();
        if (policy != null && policy.getTotalSumInsured() != null && product != null
                && product.getMaxSumAssured() != null && product.getMaxSumAssured().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ratio = policy.getTotalSumInsured()
                    .divide(product.getMaxSumAssured(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(40));
            score = score.add(ratio.min(BigDecimal.valueOf(40)));
            drivers.append("sumInsuredRatio=").append(ratio.setScale(2, RoundingMode.HALF_UP)).append("; ");
        }
        if (Boolean.TRUE.equals(product != null ? product.getManualUnderwritingRequired() : null)) {
            score = score.add(BigDecimal.valueOf(10));
            drivers.append("manualUwRequired; ");
        }
        if (uwCase.getPriority() == UwPriority.HIGH) {
            score = score.add(BigDecimal.valueOf(10));
            drivers.append("highPriority; ");
        } else if (uwCase.getPriority() == UwPriority.URGENT) {
            score = score.add(BigDecimal.valueOf(20));
            drivers.append("urgentPriority; ");
        }
        if (score.compareTo(BigDecimal.valueOf(100)) > 0) {
            score = BigDecimal.valueOf(100);
        }
        UwRiskBand band = UwRiskBand.LOW;
        if (score.compareTo(BigDecimal.valueOf(75)) >= 0) band = UwRiskBand.CRITICAL;
        else if (score.compareTo(BigDecimal.valueOf(55)) >= 0) band = UwRiskBand.HIGH;
        else if (score.compareTo(BigDecimal.valueOf(35)) >= 0) band = UwRiskBand.MEDIUM;

        if (band == UwRiskBand.HIGH || band == UwRiskBand.CRITICAL) {
            uwCase.setPriority(UwPriority.HIGH);
            caseRepository.save(uwCase);
        }

        UwAssessment assessment = UwAssessment.builder()
                .caseId(uwCase.getId())
                .assessmentType(UwAssessmentType.RISK)
                .title("Auto risk score")
                .summary("Computed risk score for underwriting prioritization")
                .score(score)
                .riskBand(band)
                .modelVersion("v1-heuristic")
                .scoreDrivers(drivers.toString())
                .assessedBy("SYSTEM")
                .assessedAt(LocalDateTime.now())
                .completed(true)
                .build();
        return assessmentRepository.save(assessment);
    }

    private UwAssessment mapAssessmentRequest(UwAssessment assessment, UwAssessmentRequest request,
                                              UwAssessmentType type, UUID caseId, String actor) {
        assessment.setCaseId(caseId);
        assessment.setAssessmentType(type);
        if (request.getTitle() != null) assessment.setTitle(request.getTitle());
        if (request.getSummary() != null) assessment.setSummary(request.getSummary());
        if (request.getFindings() != null) assessment.setFindings(request.getFindings());
        if (request.getRecommendation() != null) assessment.setRecommendation(request.getRecommendation());
        if (StringUtils.hasText(request.getRiskBand())) {
            try {
                assessment.setRiskBand(UwRiskBand.valueOf(request.getRiskBand().trim().toUpperCase()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (request.getScore() != null) assessment.setScore(request.getScore());
        if (request.getModelVersion() != null) assessment.setModelVersion(request.getModelVersion());
        if (request.getScoreDrivers() != null) assessment.setScoreDrivers(request.getScoreDrivers());
        if (request.getInspectorName() != null) assessment.setInspectorName(request.getInspectorName());
        if (request.getInspectionDate() != null) assessment.setInspectionDate(request.getInspectionDate());
        if (request.getReinsuranceCompanyId() != null) assessment.setReinsuranceCompanyId(request.getReinsuranceCompanyId());
        if (request.getReinsuranceCompanyName() != null) assessment.setReinsuranceCompanyName(request.getReinsuranceCompanyName());
        if (request.getCompleted() != null) assessment.setCompleted(request.getCompleted());
        assessment.setAssessedBy(actor);
        assessment.setAssessedAt(LocalDateTime.now());
        return assessment;
    }

    private UwAssessmentType parseAssessmentType(String type) {
        try {
            return UwAssessmentType.valueOf(type.trim().toUpperCase());
        } catch (Exception e) {
            throw new GlobalException("UW-013", type);
        }
    }

    private UnderwritingCase findCase(UUID id) {
        return caseRepository.findById(id).orElseThrow(() -> new GlobalException("UW-004", id));
    }

    private String generateCaseNumber() {
        return "UW-" + System.currentTimeMillis();
    }

    private UnderwritingCaseDto toCaseDto(UnderwritingCase c, boolean detail) {
        UnderwritingCaseDto.UnderwritingCaseDtoBuilder b = UnderwritingCaseDto.builder()
                .id(c.getId())
                .caseNumber(c.getCaseNumber())
                .policyId(c.getPolicyId())
                .policyNumber(c.getPolicyNumber())
                .customerId(c.getCustomerId())
                .customerName(c.getCustomerName())
                .productCode(c.getProductCode())
                .productName(c.getProductName())
                .lineOfBusiness(c.getLineOfBusiness())
                .caseStatus(c.getCaseStatus() != null ? c.getCaseStatus().name() : null)
                .priority(c.getPriority() != null ? c.getPriority().name() : null)
                .assignee(c.getAssignee())
                .referralReason(c.getReferralReason())
                .referralRules(c.getReferralRules())
                .submittedBy(c.getSubmittedBy())
                .submittedAt(c.getSubmittedAt())
                .dueAt(c.getDueAt())
                .closedAt(c.getClosedAt())
                .notes(c.getNotes())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt());
        if (detail) {
            decisionRepository.findByCaseIdOrderByCreatedAtDesc(c.getId()).stream().findFirst()
                    .ifPresent(d -> b.latestDecision(toDecisionDto(d)));
            b.approvalSteps(approvalStepRepository.findByCaseIdOrderByStepOrderAsc(c.getId()).stream()
                    .map(this::toApprovalDto).collect(Collectors.toList()));
            b.assessments(assessmentRepository.findByCaseIdOrderByCreatedAtDesc(c.getId()).stream()
                    .map(this::toAssessmentDto).collect(Collectors.toList()));
        }
        return b.build();
    }

    private UnderwritingDecisionDto toDecisionDto(UnderwritingDecision d) {
        return UnderwritingDecisionDto.builder()
                .id(d.getId())
                .caseId(d.getCaseId())
                .outcome(d.getOutcome() != null ? d.getOutcome().name() : null)
                .conditions(d.getConditions())
                .exclusions(d.getExclusions())
                .loadingPercent(d.getLoadingPercent())
                .loadingAmount(d.getLoadingAmount())
                .rationale(d.getRationale())
                .decidedBy(d.getDecidedBy())
                .decidedAt(d.getDecidedAt())
                .approvedBy(d.getApprovedBy())
                .approvedAt(d.getApprovedAt())
                .finalDecision(d.getFinalDecision())
                .createdAt(d.getCreatedAt())
                .build();
    }

    private UwApprovalStepDto toApprovalDto(UwApprovalStep s) {
        return UwApprovalStepDto.builder()
                .id(s.getId())
                .caseId(s.getCaseId())
                .decisionId(s.getDecisionId())
                .stepOrder(s.getStepOrder())
                .stepName(s.getStepName())
                .approverRole(s.getApproverRole())
                .assignedTo(s.getAssignedTo())
                .stepStatus(s.getStepStatus() != null ? s.getStepStatus().name() : null)
                .actedBy(s.getActedBy())
                .actedAt(s.getActedAt())
                .comments(s.getComments())
                .build();
    }

    private UwAssessmentDto toAssessmentDto(UwAssessment a) {
        return UwAssessmentDto.builder()
                .id(a.getId())
                .caseId(a.getCaseId())
                .assessmentType(a.getAssessmentType() != null ? a.getAssessmentType().name() : null)
                .title(a.getTitle())
                .summary(a.getSummary())
                .findings(a.getFindings())
                .recommendation(a.getRecommendation())
                .riskBand(a.getRiskBand() != null ? a.getRiskBand().name() : null)
                .score(a.getScore())
                .modelVersion(a.getModelVersion())
                .scoreDrivers(a.getScoreDrivers())
                .inspectorName(a.getInspectorName())
                .inspectionDate(a.getInspectionDate())
                .reinsuranceCompanyId(a.getReinsuranceCompanyId())
                .reinsuranceCompanyName(a.getReinsuranceCompanyName())
                .assessedBy(a.getAssessedBy())
                .assessedAt(a.getAssessedAt())
                .completed(a.getCompleted())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
