package com.project.smartinsurance.policyService.service.impl;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.policyService.dto.*;
import com.project.smartinsurance.policyService.mapper.PolicyMapper;
import com.project.smartinsurance.policyService.model.*;
import com.project.smartinsurance.policyService.model.enums.*;
import com.project.smartinsurance.policyService.repository.*;
import com.project.smartinsurance.policyService.service.PolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyVersionRepository policyVersionRepository;
    private final CoverageRepository coverageRepository;
    private final PolicyPremiumRepository policyPremiumRepository;
    private final EndorsementRepository endorsementRepository;
    private final PolicyAuditTrailRepository policyAuditTrailRepository;
    private final PolicyMapper policyMapper;
    private final com.project.smartinsurance.billingService.service.PremiumLifecycleService premiumLifecycleService;
    private final com.project.smartinsurance.productService.service.ProductService productService;
    private final com.project.smartinsurance.underwritingService.service.UnderwritingService underwritingService;

    @Override
    @Transactional
    public PolicyDto createPolicy(PolicyCreateRequest request) {
        // Draft policies may reference catalog products that are not yet ACTIVE;
        // issuePolicy still requires an ACTIVE product.
        validateProductAndPlan(request.getProductCode(), request.getPlanCode(), false);

        String policyNumber = generatePolicyNumber();

        Policy policy = Policy.builder()
                .policyNumber(policyNumber)
                .productCode(request.getProductCode())
                .planCode(request.getPlanCode())
                .policyStatus(PolicyStatus.DRAFT)
                .customerId(request.getCustomerId())
                .customerName(request.getCustomerName())
                .insuredPartyId(request.getInsuredPartyId())
                .insuredPartyName(request.getInsuredPartyName())
                .insuredPartyType(request.getInsuredPartyType())
                .channel(request.getChannel() != null ? PolicyChannel.valueOf(request.getChannel()) : null)
                .branchId(request.getBranchId())
                .inceptionDate(request.getInceptionDate())
                .effectiveDate(request.getEffectiveDate())
                .expiryDate(request.getExpiryDate())
                .issueDate(request.getIssueDate())
                .totalSumInsured(request.getTotalSumInsured())
                .basePremium(request.getBasePremium())
                .totalPremium(request.getTotalPremium() != null ? request.getTotalPremium() : request.getBasePremium())
                .currencyCode(request.getCurrencyCode())
                .paymentFrequency(request.getPaymentFrequency())
                .createdBy(request.getCreatedBy())
                .deleted(false)
                .build();

        Policy savedPolicy = policyRepository.save(policy);

        if (request.getCoverages() != null) {
            List<Coverage> coverages = request.getCoverages().stream()
                    .map(covDto -> Coverage.builder()
                            .policy(savedPolicy)
                            .coverageCode(covDto.getCoverageCode())
                            .coverageName(covDto.getCoverageName())
                            .sumInsured(covDto.getSumInsured())
                            .deductible(covDto.getDeductible())
                            .limitAmount(covDto.getLimitAmount())
                            .premiumAmount(covDto.getPremiumAmount())
                            .coverageStatus(covDto.getStatus() != null ? CoverageStatus.valueOf(covDto.getStatus()) : CoverageStatus.ACTIVE)
                            .waitingPeriod(covDto.getWaitingPeriod())
                            .build())
                    .collect(Collectors.toList());
            coverageRepository.saveAll(coverages);
        }

        if (request.getPremiums() != null) {
            List<PolicyPremium> premiums = request.getPremiums().stream()
                    .map(prmDto -> PolicyPremium.builder()
                            .policy(savedPolicy)
                            .basePremium(prmDto.getBasePremium())
                            .discountAmount(prmDto.getDiscountAmount())
                            .taxAmount(prmDto.getTaxAmount())
                            .surchargeAmount(prmDto.getSurchargeAmount())
                            .feeAmount(prmDto.getFeeAmount())
                            .totalPremium(prmDto.getTotalPremium())
                            .payableAmount(prmDto.getPayableAmount())
                            .installmentNumber(prmDto.getInstallmentNumber())
                            .dueDate(prmDto.getDueDate())
                            .paidDate(prmDto.getPaidDate())
                            .paymentStatus(prmDto.getPaymentStatus())
                            .build())
                    .collect(Collectors.toList());
            policyPremiumRepository.saveAll(premiums);
        }

        log.info("Policy created successfully with number: {}", policyNumber);
        return enrichPolicyDto(savedPolicy);
    }

    @Override
    @Transactional
    public PolicyDto updatePolicy(UUID id, PolicyUpdateRequest request) {
        Policy policy = findPolicyOrThrow(id);
        String productCode = request.getProductCode() != null ? request.getProductCode() : policy.getProductCode();
        String planCode = request.getPlanCode() != null ? request.getPlanCode() : policy.getPlanCode();
        if (request.getProductCode() != null || request.getPlanCode() != null) {
            validateProductAndPlan(productCode, planCode, false);
        }
        policyMapper.updateEntity(policy, request);
        policy = policyRepository.save(policy);
        log.info("Policy updated: {}", id);
        return enrichPolicyDto(policy);
    }

    @Override
    @Transactional
    public void deletePolicy(UUID id) {
        Policy policy = findPolicyOrThrow(id);
        policy.setDeleted(true);
        policy.setDeletedAt(LocalDateTime.now());
        policy.setDeletedBy("SYSTEM");
        policyRepository.save(policy);
        log.info("Policy soft deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyDto getPolicyById(UUID id) {
        Policy policy = findPolicyOrThrow(id);
        return enrichPolicyDto(policy);
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyDto getPolicyByNumber(String policyNumber) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new GlobalException("POL-001", policyNumber));
        return enrichPolicyDto(policy);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<PolicyDto> searchPolicies(PolicySearchRequest request) {
        Sort sort = request.getSortDir().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Policy> page;

        if (request.getPolicyNumber() != null) {
            page = policyRepository.findByPolicyNumber(request.getPolicyNumber())
                    .map(p -> Page.<Policy>empty(pageable))
                    .orElse(Page.<Policy>empty(pageable));
        } else if (request.getCustomerId() != null) {
            page = policyRepository.findByCustomerId(request.getCustomerId(), pageable);
        } else if (request.getPolicyStatus() != null) {
            PolicyStatus status = PolicyStatus.valueOf(request.getPolicyStatus());
            page = policyRepository.findByPolicyStatus(status, pageable);
        } else if (request.getBranchId() != null) {
            page = policyRepository.findByBranchId(request.getBranchId(), pageable);
        } else if (request.getEffectiveDateFrom() != null && request.getEffectiveDateTo() != null) {
            page = policyRepository.findByEffectiveDateBetween(request.getEffectiveDateFrom(), request.getEffectiveDateTo(), pageable);
        } else {
            page = policyRepository.findAll(pageable);
        }

        List<PolicyDto> content = page.getContent().stream()
                .map(this::enrichPolicyDto)
                .collect(Collectors.toList());

        return PagedData.<PolicyDto>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<PolicyDto> getAllPolicies(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Policy> policyPage = policyRepository.findAll(pageable);

        List<PolicyDto> content = policyPage.getContent().stream()
                .map(this::enrichPolicyDto)
                .collect(Collectors.toList());

        return PagedData.<PolicyDto>builder()
                .content(content)
                .page(policyPage.getNumber())
                .size(policyPage.getSize())
                .totalElements(policyPage.getTotalElements())
                .totalPages(policyPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<PolicyDto> getPoliciesByCustomer(UUID customerId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Policy> policyPage = policyRepository.findByCustomerId(customerId, pageable);

        List<PolicyDto> content = policyPage.getContent().stream()
                .map(this::enrichPolicyDto)
                .collect(Collectors.toList());

        return PagedData.<PolicyDto>builder()
                .content(content)
                .page(policyPage.getNumber())
                .size(policyPage.getSize())
                .totalElements(policyPage.getTotalElements())
                .totalPages(policyPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<PolicyDto> getPoliciesByStatus(String status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PolicyStatus policyStatus = PolicyStatus.valueOf(status);
        Page<Policy> policyPage = policyRepository.findByPolicyStatus(policyStatus, pageable);

        List<PolicyDto> content = policyPage.getContent().stream()
                .map(this::enrichPolicyDto)
                .collect(Collectors.toList());

        return PagedData.<PolicyDto>builder()
                .content(content)
                .page(policyPage.getNumber())
                .size(policyPage.getSize())
                .totalElements(policyPage.getTotalElements())
                .totalPages(policyPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public PolicyDto cancelPolicy(UUID id, CancelPolicyRequest request) {
        Policy policy = findPolicyOrThrow(id);

        if (policy.getPolicyStatus() == PolicyStatus.CANCELLED) {
            throw new GlobalException("POL-002", id);
        }

        policy.setPolicyStatus(PolicyStatus.CANCELLED);
        policy.setCancellationDate(request.getEffectiveDate());
        policy.setCancellationReason(request.getReason());
        policy = policyRepository.save(policy);
        try {
            premiumLifecycleService.cancelOpenInstallments(id);
        } catch (Exception e) {
            log.warn("Cancel open installments failed for {}: {}", id, e.getMessage());
        }
        log.info("Policy cancelled: {}", id);
        return enrichPolicyDto(policy);
    }

    @Override
    @Transactional
    public PolicyDto reinstatePolicy(UUID id, ReinstatePolicyRequest request) {
        Policy policy = findPolicyOrThrow(id);

        if (policy.getPolicyStatus() != PolicyStatus.CANCELLED && policy.getPolicyStatus() != PolicyStatus.LAPSED) {
            throw new GlobalException("POL-003", id);
        }

        policy.setPolicyStatus(PolicyStatus.REINSTATED);
        policy.setReinstatementDate(LocalDate.now());
        policy = policyRepository.save(policy);
        try {
            premiumLifecycleService.generateBackPremiumOnReinstate(id);
        } catch (Exception e) {
            log.warn("Back-premium on reinstate failed for {}: {}", id, e.getMessage());
        }
        log.info("Policy reinstated: {}", id);
        return enrichPolicyDto(policy);
    }

    @Override
    @Transactional
    public PolicyDto issuePolicy(UUID id) {
        Policy policy = findPolicyOrThrow(id);
        if (policy.getPolicyStatus() == PolicyStatus.PENDING_APPROVAL) {
            if (!underwritingService.isUnderwritingCleared(id)) {
                throw new GlobalException("UW-014", id);
            }
            // Cleared by UW approval path already activates; treat as already issued
            throw new GlobalException("UW-015", id);
        }
        if (policy.getPolicyStatus() != PolicyStatus.DRAFT) {
            throw new GlobalException("POL-004", id);
        }
        if (underwritingService.requiresUnderwriting(policy.getProductCode())) {
            throw new GlobalException("UW-016", policy.getProductCode());
        }
        validateProductAndPlan(policy.getProductCode(), policy.getPlanCode(), true);
        policy.setPolicyStatus(PolicyStatus.ACTIVE);
        policy.setIssueDate(LocalDate.now());
        policy = policyRepository.save(policy);
        try {
            premiumLifecycleService.generateSchedule(policy.getId(), false);
        } catch (Exception e) {
            log.warn("Premium schedule generation on issue failed for {}: {}", id, e.getMessage());
        }
        log.info("Policy issued: {}", id);
        return enrichPolicyDto(policy);
    }

    @Override
    @Transactional
    public PolicyDto suspendPolicy(UUID id, CancelPolicyRequest request) {
        Policy policy = findPolicyOrThrow(id);
        if (policy.getPolicyStatus() != PolicyStatus.ACTIVE) {
            throw new GlobalException("POL-004", id);
        }
        policy.setPolicyStatus(PolicyStatus.SUSPENDED);
        policy.setCancellationReason(request.getReason());
        policy = policyRepository.save(policy);
        log.info("Policy suspended: {}", id);
        return enrichPolicyDto(policy);
    }

    @Override
    @Transactional
    public PolicyDto transferPolicy(UUID id, TransferPolicyRequest request) {
        Policy policy = findPolicyOrThrow(id);
        policy.setCustomerId(UUID.fromString(request.getNewCustomerId()));
        if (request.getNewCustomerName() != null) {
            policy.setCustomerName(request.getNewCustomerName());
        }
        policy = policyRepository.save(policy);
        log.info("Policy transferred: {} to customer {}", id, request.getNewCustomerId());
        return enrichPolicyDto(policy);
    }

    @Override
    @Transactional
    public PolicyDto renewPolicy(UUID id, RenewPolicyRequest request) {
        Policy policy = findPolicyOrThrow(id);
        if (policy.getPolicyStatus() != PolicyStatus.ACTIVE && policy.getPolicyStatus() != PolicyStatus.EXPIRED) {
            throw new GlobalException("POL-004", id);
        }
        policy.setExpiryDate(request.getNewExpiryDate());
        policy.setRenewalCount(policy.getRenewalCount() + 1);
        if (request.getRenewalPremium() != null) {
            policy.setTotalPremium(request.getRenewalPremium());
        }
        policy = policyRepository.save(policy);
        try {
            premiumLifecycleService.generateSchedule(id, false);
        } catch (Exception e) {
            log.warn("Premium schedule generation on renew failed for {}: {}", id, e.getMessage());
        }
        log.info("Policy renewed: {}", id);
        return enrichPolicyDto(policy);
    }

    @Override
    @Transactional
    public PolicyDto upgradePolicy(UUID id, UpgradePolicyRequest request) {
        Policy policy = findPolicyOrThrow(id);
        if (policy.getPolicyStatus() != PolicyStatus.ACTIVE) {
            throw new GlobalException("POL-004", id);
        }
        if (request.getNewPlanCode() != null) {
            policy.setPlanCode(request.getNewPlanCode());
        }
        BigDecimal delta = BigDecimal.ZERO;
        if (request.getAdditionalSumInsured() != null) {
            BigDecimal current = policy.getTotalSumInsured() != null ? policy.getTotalSumInsured() : BigDecimal.ZERO;
            policy.setTotalSumInsured(current.add(request.getAdditionalSumInsured()));
            // Approximate mid-term debit: 1% of additional SI as premium delta when no calc rule applied
            delta = request.getAdditionalSumInsured().multiply(new BigDecimal("0.01")).setScale(2, java.math.RoundingMode.HALF_UP);
        }
        policy = policyRepository.save(policy);
        if (delta.compareTo(BigDecimal.ZERO) > 0) {
            try {
                premiumLifecycleService.applyEndorsementDelta(id, delta, "Upgrade additional sum insured");
            } catch (Exception e) {
                log.warn("Endorsement premium delta on upgrade failed for {}: {}", id, e.getMessage());
            }
        }
        log.info("Policy upgraded: {}", id);
        return enrichPolicyDto(policy);
    }

    @Override
    @Transactional
    public PolicyDto downgradePolicy(UUID id, DowngradePolicyRequest request) {
        Policy policy = findPolicyOrThrow(id);
        if (policy.getPolicyStatus() != PolicyStatus.ACTIVE) {
            throw new GlobalException("POL-004", id);
        }
        if (request.getNewPlanCode() != null) {
            policy.setPlanCode(request.getNewPlanCode());
        }
        if (request.getReducedSumInsured() != null) {
            policy.setTotalSumInsured(request.getReducedSumInsured());
        }
        policy = policyRepository.save(policy);
        log.info("Policy downgraded: {}", id);
        return enrichPolicyDto(policy);
    }

    @Override
    @Transactional
    public PolicyDto approvePolicy(UUID id, String approvedBy) {
        Policy policy = findPolicyOrThrow(id);

        if (policy.getPolicyStatus() != PolicyStatus.PENDING_APPROVAL && policy.getPolicyStatus() != PolicyStatus.DRAFT) {
            throw new GlobalException("POL-004", id);
        }

        policy.setPolicyStatus(PolicyStatus.ACTIVE);
        policy.setApprovedBy(approvedBy);
        policy.setApprovalDate(LocalDateTime.now());
        policy.setApprovalStatus("APPROVED");
        policy = policyRepository.save(policy);

        log.info("Policy approved: {}", id);
        return enrichPolicyDto(policy);
    }

    @Override
    @Transactional
    public EndorsementDto createEndorsement(UUID policyId, EndorsementRequest request) {
        Policy policy = findPolicyOrThrow(policyId);

        String endorsementNumber = generateEndorsementNumber();

        List<PolicyVersion> versions = policyVersionRepository.findByPolicyIdOrderByVersionNumberDesc(policyId);
        int latestVersion = versions.isEmpty() ? 0 : versions.get(0).getVersionNumber();

        Endorsement endorsement = Endorsement.builder()
                .policy(policy)
                .endorsementNumber(endorsementNumber)
                .endorsementType(request.getEndorsementType() != null ? EndorsementType.valueOf(request.getEndorsementType()) : EndorsementType.GENERAL)
                .endorsementStatus(EndorsementStatus.PENDING)
                .previousVersion(latestVersion)
                .newVersion(latestVersion + 1)
                .changeDescription(request.getChangeDescription())
                .premiumDifference(request.getPremiumDifference())
                .effectiveDate(request.getEffectiveDate())
                .requestedBy(request.getRequestedBy())
                .build();

        endorsement = endorsementRepository.save(endorsement);
        log.info("Endorsement created: {} for policy: {}", endorsementNumber, policyId);
        return policyMapper.toEndorsementDto(endorsement);
    }

    @Override
    @Transactional
    public EndorsementDto approveEndorsement(UUID endorsementId, String approvedBy) {
        Endorsement endorsement = endorsementRepository.findById(endorsementId)
                .orElseThrow(() -> new GlobalException("POL-005", endorsementId));

        if (endorsement.getEndorsementStatus() != EndorsementStatus.PENDING) {
            throw new GlobalException("POL-006", endorsementId);
        }

        endorsement.setEndorsementStatus(EndorsementStatus.APPROVED);
        endorsement.setApprovedBy(approvedBy);
        endorsement.setApprovedAt(LocalDateTime.now());
        endorsement = endorsementRepository.save(endorsement);

        Policy policy = endorsement.getPolicy();
        PolicyVersion version = PolicyVersion.builder()
                .policy(policy)
                .versionNumber(endorsement.getNewVersion())
                .changeType(endorsement.getEndorsementType().name())
                .sourceReference(endorsement.getEndorsementNumber())
                .effectiveFrom(endorsement.getEffectiveDate())
                .snapshotData(endorsement.getChangeDescription())
                .createdBy(approvedBy)
                .build();
        policyVersionRepository.save(version);

        endorsement.setEndorsementStatus(EndorsementStatus.EFFECTIVE);
        endorsement = endorsementRepository.save(endorsement);

        log.info("Endorsement approved and made effective: {}", endorsementId);
        return policyMapper.toEndorsementDto(endorsement);
    }

    @Override
    @Transactional
    public EndorsementDto rejectEndorsement(UUID endorsementId, String rejectedBy, String reason) {
        Endorsement endorsement = endorsementRepository.findById(endorsementId)
                .orElseThrow(() -> new GlobalException("POL-005", endorsementId));

        if (endorsement.getEndorsementStatus() != EndorsementStatus.PENDING) {
            throw new GlobalException("POL-006", endorsementId);
        }

        endorsement.setEndorsementStatus(EndorsementStatus.REJECTED);
        endorsement.setRejectedBy(rejectedBy);
        endorsement.setRejectedAt(LocalDateTime.now());
        endorsement.setRejectionReason(reason);
        endorsement = endorsementRepository.save(endorsement);

        log.info("Endorsement rejected: {}", endorsementId);
        return policyMapper.toEndorsementDto(endorsement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EndorsementDto> getEndorsementsByPolicy(UUID policyId) {
        return endorsementRepository.findByPolicyIdOrderByCreatedAtDesc(policyId).stream()
                .map(policyMapper::toEndorsementDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoverageDto> getCoveragesByPolicy(UUID policyId) {
        return coverageRepository.findByPolicyId(policyId).stream()
                .map(policyMapper::toCoverageDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyPremiumDto> getPremiumsByPolicy(UUID policyId) {
        return policyPremiumRepository.findByPolicyIdOrderByDueDateAsc(policyId).stream()
                .map(policyMapper::toPremiumDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyVersionDto> getVersionsByPolicy(UUID policyId) {
        return policyVersionRepository.findByPolicyIdOrderByVersionNumberDesc(policyId).stream()
                .map(policyMapper::toVersionDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyAuditEntryDto> getAuditTrail(String entityType, String entityId) {
        return policyAuditTrailRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType, entityId).stream()
                .map(policyMapper::toAuditDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PolicySummaryResponse getPolicySummary() {
        long totalPolicies = policyRepository.count();
        long activePolicies = policyRepository.countByPolicyStatus(PolicyStatus.ACTIVE);
        long draftPolicies = policyRepository.countByPolicyStatus(PolicyStatus.DRAFT);
        long expiredPolicies = policyRepository.countByPolicyStatus(PolicyStatus.EXPIRED);
        long cancelledPolicies = policyRepository.countByPolicyStatus(PolicyStatus.CANCELLED);
        long suspendedPolicies = policyRepository.countByPolicyStatus(PolicyStatus.SUSPENDED);
        long lapsedPolicies = policyRepository.countByPolicyStatus(PolicyStatus.LAPSED);

        BigDecimal totalPremiumAmount = BigDecimal.ZERO;
        BigDecimal totalSumInsured = BigDecimal.ZERO;
        double averagePremium = 0.0;

        List<Policy> allPolicies = policyRepository.findAll();
        for (Policy p : allPolicies) {
            if (p.getTotalPremium() != null) {
                totalPremiumAmount = totalPremiumAmount.add(p.getTotalPremium());
            }
            if (p.getTotalSumInsured() != null) {
                totalSumInsured = totalSumInsured.add(p.getTotalSumInsured());
            }
        }

        if (totalPolicies > 0) {
            averagePremium = totalPremiumAmount.divide(BigDecimal.valueOf(totalPolicies), BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        return PolicySummaryResponse.builder()
                .totalPolicies(totalPolicies)
                .activePolicies(activePolicies)
                .draftPolicies(draftPolicies)
                .expiredPolicies(expiredPolicies)
                .cancelledPolicies(cancelledPolicies)
                .suspendedPolicies(suspendedPolicies)
                .lapsedPolicies(lapsedPolicies)
                .totalPremiumAmount(totalPremiumAmount)
                .totalSumInsured(totalSumInsured)
                .averagePremium(averagePremium)
                .build();
    }

    private PolicyDto enrichPolicyDto(Policy policy) {
        PolicyDto dto = policyMapper.toDto(policy);
        dto.setCoverages(coverageRepository.findByPolicyId(policy.getId()).stream()
                .map(policyMapper::toCoverageDto)
                .collect(Collectors.toList()));
        dto.setPremiums(policyPremiumRepository.findByPolicyIdOrderByDueDateAsc(policy.getId()).stream()
                .map(policyMapper::toPremiumDto)
                .collect(Collectors.toList()));
        dto.setEndorsements(endorsementRepository.findByPolicyIdOrderByCreatedAtDesc(policy.getId()).stream()
                .map(policyMapper::toEndorsementDto)
                .collect(Collectors.toList()));
        return dto;
    }

    /**
     * Resolves product/plan against the live catalog. When requireActive is true (issue),
     * product must be ACTIVE; otherwise product must exist (create/edit may use draft products).
     */
    private void validateProductAndPlan(String productCode, String planCode, boolean requireActive) {
        if (productCode == null || productCode.isBlank()) {
            throw new GlobalException("POL-008", productCode != null ? productCode : "null");
        }
        com.project.smartinsurance.productService.dto.ProductDto product;
        try {
            product = productService.getProductByCode(productCode);
        } catch (GlobalException ex) {
            throw new GlobalException("POL-008", productCode);
        }
        String status = product.getProductStatus() != null ? product.getProductStatus() : product.getStatus();
        if (requireActive && !"ACTIVE".equalsIgnoreCase(status)) {
            throw new GlobalException("POL-008", productCode);
        }
        if (planCode != null && !planCode.isBlank()) {
            var plans = productService.getPlansByProduct(product.getId());
            boolean planOk = plans.stream().anyMatch(p -> planCode.equalsIgnoreCase(p.getCode()));
            if (!planOk) {
                throw new GlobalException("POL-009", planCode, productCode);
            }
        }
    }

    private Policy findPolicyOrThrow(UUID id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new GlobalException("POL-001", id));
    }

    private String generatePolicyNumber() {
        long count = policyRepository.count() + 1;
        String number;
        do {
            number = String.format("POL-%06d", count);
            count++;
        } while (policyRepository.existsByPolicyNumber(number));
        return number;
    }

    private String generateEndorsementNumber() {
        long count = endorsementRepository.count() + 1;
        return String.format("END-%06d", count);
    }
}
