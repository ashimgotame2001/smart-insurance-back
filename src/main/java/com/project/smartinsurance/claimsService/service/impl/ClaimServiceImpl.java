package com.project.smartinsurance.claimsService.service.impl;

import com.project.smartinsurance.claimsService.dto.*;
import com.project.smartinsurance.claimsService.model.*;
import com.project.smartinsurance.claimsService.model.enums.*;
import com.project.smartinsurance.claimsService.repository.*;
import com.project.smartinsurance.claimsService.service.ClaimService;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.dto.PageFilterRequest;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.commonService.utils.PageableUtils;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private static final BigDecimal CHECKER_THRESHOLD = new BigDecimal("100000");
    private static final Set<ClaimStatus> TERMINAL_FOR_UPDATE = EnumSet.of(
            ClaimStatus.PENDING_APPROVAL, ClaimStatus.APPROVED, ClaimStatus.REJECTED,
            ClaimStatus.SETTLED, ClaimStatus.PAID, ClaimStatus.CLOSED, ClaimStatus.LITIGATION);
    private static final Set<ClaimStatus> OPEN_STATUSES = EnumSet.of(
            ClaimStatus.INTIMATED, ClaimStatus.REGISTERED, ClaimStatus.UNDER_VERIFICATION,
            ClaimStatus.DOCS_PENDING, ClaimStatus.UNDER_INVESTIGATION, ClaimStatus.SURVEY_PENDING,
            ClaimStatus.MEDICAL_REVIEW, ClaimStatus.PENDING_APPROVAL, ClaimStatus.APPROVED,
            ClaimStatus.REOPENED, ClaimStatus.ON_HOLD);

    private final ClaimRepository claimRepository;
    private final ClaimCoverageItemRepository coverageItemRepository;
    private final ClaimPartyRepository partyRepository;
    private final ClaimDocumentRepository documentRepository;
    private final ClaimReserveRepository reserveRepository;
    private final ClaimInvestigationRepository investigationRepository;
    private final ClaimFraudCaseRepository fraudCaseRepository;
    private final ClaimSurveyRepository surveyRepository;
    private final ClaimMedicalReviewRepository medicalReviewRepository;
    private final ClaimDecisionRepository decisionRepository;
    private final ClaimSettlementRepository settlementRepository;
    private final ClaimPaymentRepository paymentRepository;
    private final ClaimRecoveryRepository recoveryRepository;
    private final ClaimSalvageRepository salvageRepository;
    private final ClaimEventRepository eventRepository;
    private final ClaimChecklistTemplateRepository checklistTemplateRepository;

    @PostConstruct
    @Transactional
    public void seedChecklistTemplates() {
        if (checklistTemplateRepository.count() > 0) {
            return;
        }
        List<ClaimChecklistTemplate> seeds = new ArrayList<>();
        seeds.add(tpl(ClaimType.MOTOR, "FIR", "Police FIR / Accident Report", true));
        seeds.add(tpl(ClaimType.MOTOR, "RC", "Vehicle Registration Certificate", true));
        seeds.add(tpl(ClaimType.MOTOR, "DL", "Driving License", true));
        seeds.add(tpl(ClaimType.MOTOR, "PHOTOS", "Damage Photographs", true));
        seeds.add(tpl(ClaimType.MOTOR, "ESTIMATE", "Repair Estimate", false));
        seeds.add(tpl(ClaimType.HEALTH, "DISCHARGE", "Hospital Discharge Summary", true));
        seeds.add(tpl(ClaimType.HEALTH, "BILLS", "Hospital Bills", true));
        seeds.add(tpl(ClaimType.HEALTH, "PRESCRIPTION", "Prescription / Treatment Notes", true));
        seeds.add(tpl(ClaimType.HEALTH, "LAB", "Lab / Diagnostic Reports", false));
        seeds.add(tpl(ClaimType.LIFE_DEATH, "DEATH_CERT", "Death Certificate", true));
        seeds.add(tpl(ClaimType.LIFE_DEATH, "CLAIM_FORM", "Death Claim Form", true));
        seeds.add(tpl(ClaimType.LIFE_DEATH, "ID_PROOF", "Nominee ID Proof", true));
        seeds.add(tpl(ClaimType.LIFE_DEATH, "BANK", "Nominee Bank Details", true));
        checklistTemplateRepository.saveAll(seeds);
        log.info("Seeded {} claim checklist templates", seeds.size());
    }

    private ClaimChecklistTemplate tpl(ClaimType type, String docType, String name, boolean required) {
        return ClaimChecklistTemplate.builder()
                .claimType(type)
                .documentType(docType)
                .documentName(name)
                .required(required)
                .build();
    }

    @Override
    @Transactional
    public ClaimDto registerClaim(ClaimCreateRequest request) {
        if (request.getPolicyId() != null && request.getLossDate() != null && request.getClaimType() != null) {
            LocalDate from = request.getLossDate().minusDays(7);
            LocalDate to = request.getLossDate().plusDays(7);
            if (claimRepository.existsByPolicyIdAndClaimTypeAndLossDateBetweenAndDeletedFalse(
                    request.getPolicyId(), request.getClaimType(), from, to)) {
                throw new GlobalException("CLM-002");
            }
        }

        String claimNumber = generateClaimNumber();
        LocalDateTime now = LocalDateTime.now();
        Claim claim = Claim.builder()
                .claimNumber(claimNumber)
                .policyId(request.getPolicyId())
                .policyNumber(request.getPolicyNumber())
                .customerId(request.getCustomerId())
                .customerName(request.getCustomerName())
                .insuredPartyId(request.getInsuredPartyId())
                .insuredPartyName(request.getInsuredPartyName())
                .claimType(request.getClaimType())
                .lossDate(request.getLossDate())
                .lossTime(request.getLossTime())
                .lossLocation(request.getLossLocation())
                .causeOfLoss(request.getCauseOfLoss())
                .description(request.getDescription())
                .estimatedLoss(request.getEstimatedLoss())
                .intimatedAt(request.getIntimatedAt() != null ? request.getIntimatedAt() : now)
                .reportedBy(request.getReportedBy())
                .branchId(request.getBranchId())
                .claimStatus(ClaimStatus.REGISTERED)
                .reserveAmount(request.getEstimatedLoss())
                .currency("NPR")
                .priority(request.getPriority() != null ? request.getPriority() : ClaimPriority.NORMAL)
                .fraudFlag(false)
                .catastropheCode(request.getCatastropheCode())
                .policeFirNumber(request.getPoliceFirNumber())
                .hospitalAdmissionDate(request.getHospitalAdmissionDate())
                .litigationFlag(false)
                .deleted(false)
                .paidAmount(BigDecimal.ZERO)
                .build();
        claim.setStatus(Status.ACTIVE);
        claim = claimRepository.save(claim);

        if (request.getEstimatedLoss() != null) {
            ClaimReserve reserve = ClaimReserve.builder()
                    .claim(claim)
                    .amount(request.getEstimatedLoss())
                    .previousAmount(BigDecimal.ZERO)
                    .reason("Initial reserve on registration")
                    .revisedBy(request.getReportedBy())
                    .revisedAt(now)
                    .build();
            reserveRepository.save(reserve);
        }

        if (request.getCoverages() != null) {
            for (ClaimCoverageItemDto c : request.getCoverages()) {
                coverageItemRepository.save(ClaimCoverageItem.builder()
                        .claim(claim)
                        .coverageCode(c.getCoverageCode())
                        .coverageName(c.getCoverageName())
                        .sumInsured(c.getSumInsured())
                        .claimedAmount(c.getClaimedAmount())
                        .deductible(c.getDeductible())
                        .coinsurancePercent(c.getCoinsurancePercent())
                        .approvedAmount(c.getApprovedAmount())
                        .build());
            }
        }

        if (request.getParties() != null) {
            for (ClaimPartyDto p : request.getParties()) {
                partyRepository.save(ClaimParty.builder()
                        .claim(claim)
                        .role(p.getRole())
                        .name(p.getName())
                        .contactPhone(p.getContactPhone())
                        .contactEmail(p.getContactEmail())
                        .bankName(p.getBankName())
                        .bankAccount(p.getBankAccount())
                        .bankIfsc(p.getBankIfsc())
                        .referenceId(p.getReferenceId())
                        .build());
            }
        }

        List<ClaimChecklistTemplate> templates = checklistTemplateRepository.findByClaimType(request.getClaimType());
        for (ClaimChecklistTemplate t : templates) {
            documentRepository.save(ClaimDocument.builder()
                    .claim(claim)
                    .documentType(t.getDocumentType())
                    .documentName(t.getDocumentName())
                    .documentStatus(ClaimDocumentStatus.PENDING)
                    .required(Boolean.TRUE.equals(t.getRequired()))
                    .build());
        }

        appendEvent(claim, "INTIMATED", null, ClaimStatus.INTIMATED.name(),
                "Claim intimated", request.getReportedBy());
        appendEvent(claim, "REGISTERED", ClaimStatus.INTIMATED.name(), ClaimStatus.REGISTERED.name(),
                "Claim registered as " + claimNumber, request.getReportedBy());

        log.info("Claim registered: {}", claimNumber);
        return toDto(claim, true);
    }

    @Override
    @Transactional
    public ClaimDto updateClaim(UUID id, ClaimUpdateRequest request) {
        Claim claim = findActive(id);
        if (TERMINAL_FOR_UPDATE.contains(claim.getClaimStatus())) {
            throw new GlobalException("CLM-003", claim.getClaimStatus());
        }

        if (request.getLossLocation() != null) claim.setLossLocation(request.getLossLocation());
        if (request.getCauseOfLoss() != null) claim.setCauseOfLoss(request.getCauseOfLoss());
        if (request.getDescription() != null) claim.setDescription(request.getDescription());
        if (request.getEstimatedLoss() != null) claim.setEstimatedLoss(request.getEstimatedLoss());
        if (request.getLossDate() != null) claim.setLossDate(request.getLossDate());
        if (request.getLossTime() != null) claim.setLossTime(request.getLossTime());
        if (request.getReportedBy() != null) claim.setReportedBy(request.getReportedBy());
        if (request.getBranchId() != null) claim.setBranchId(request.getBranchId());
        if (request.getPriority() != null) claim.setPriority(request.getPriority());
        if (request.getCatastropheCode() != null) claim.setCatastropheCode(request.getCatastropheCode());
        if (request.getPoliceFirNumber() != null) claim.setPoliceFirNumber(request.getPoliceFirNumber());
        if (request.getHospitalAdmissionDate() != null) claim.setHospitalAdmissionDate(request.getHospitalAdmissionDate());
        if (request.getCustomerName() != null) claim.setCustomerName(request.getCustomerName());
        if (request.getInsuredPartyName() != null) claim.setInsuredPartyName(request.getInsuredPartyName());

        claim = claimRepository.save(claim);
        appendEvent(claim, "UPDATED", claim.getClaimStatus().name(), claim.getClaimStatus().name(),
                "Claim details updated", request.getReportedBy());
        return toDto(claim, true);
    }

    @Override
    @Transactional(readOnly = true)
    public ClaimDto getById(UUID id) {
        return toDto(findActive(id), true);
    }

    @Override
    @Transactional(readOnly = true)
    public ClaimDto getByClaimNumber(String claimNumber) {
        Claim claim = claimRepository.findByClaimNumberAndDeletedFalse(claimNumber)
                .orElseThrow(() -> new GlobalException("CLM-001", claimNumber));
        return toDto(claim, true);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<ClaimDto> search(ClaimSearchRequest request) {
        Pageable pageable = PageableUtils.toPageable(request);
        Specification<Claim> spec = buildSearchSpec(request);
        Page<Claim> page = claimRepository.findAll(spec, pageable);
        return PageableUtils.toPagedData(page.map(c -> toDto(c, false)));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<ClaimDto> list(int page, int size, String sortBy, String sortDir) {
        PageFilterRequest filter = new PageFilterRequest();
        filter.setPage(page);
        filter.setSize(size);
        filter.setSortBy(sortBy);
        filter.setSortDir(sortDir);
        Pageable pageable = PageableUtils.toPageable(filter);
        Page<Claim> result = claimRepository.findByDeletedFalse(pageable);
        return PageableUtils.toPagedData(result.map(c -> toDto(c, false)));
    }

    @Override
    @Transactional
    public void softDelete(UUID id) {
        Claim claim = findActive(id);
        claim.setDeleted(true);
        claim.setDeletedAt(LocalDateTime.now());
        claim.setStatus(Status.INACTIVE);
        claimRepository.save(claim);
        appendEvent(claim, "DELETED", claim.getClaimStatus().name(), claim.getClaimStatus().name(),
                "Claim soft-deleted", null);
    }

    @Override
    @Transactional
    public ClaimDto verify(UUID id, ClaimVerifyRequest request) {
        Claim claim = findActive(id);
        ClaimStatus from = claim.getClaimStatus();
        claim.setEligibilityPassed(request.getEligibilityPassed());
        claim.setVerificationNotes(request.getNotes());

        boolean missingRequired = hasMissingRequiredDocs(claim.getId());
        ClaimStatus to = missingRequired ? ClaimStatus.DOCS_PENDING : ClaimStatus.UNDER_VERIFICATION;
        claim.setClaimStatus(to);
        claim = claimRepository.save(claim);
        appendEvent(claim, "VERIFY", from.name(), to.name(),
                "Verification: eligibility=" + request.getEligibilityPassed(), null);
        return toDto(claim, true);
    }

    @Override
    @Transactional
    public ClaimDocumentDto addDocument(UUID id, ClaimDocumentRequest request) {
        Claim claim = findActive(id);
        ClaimDocument doc = ClaimDocument.builder()
                .claim(claim)
                .documentType(request.getDocumentType())
                .documentName(request.getDocumentName())
                .documentId(request.getDocumentId())
                .documentStatus(ClaimDocumentStatus.PENDING)
                .remarks(request.getRemarks())
                .required(request.getRequired() != null ? request.getRequired() : false)
                .build();
        doc = documentRepository.save(doc);
        appendEvent(claim, "DOCUMENT_ADDED", claim.getClaimStatus().name(), claim.getClaimStatus().name(),
                "Document added: " + request.getDocumentType(), null);
        return toDocumentDto(doc);
    }

    @Override
    @Transactional
    public ClaimDocumentDto verifyDocument(UUID id, UUID docId, ClaimDocumentVerifyRequest request) {
        Claim claim = findActive(id);
        ClaimDocument doc = documentRepository.findByIdAndClaimId(docId, id)
                .orElseThrow(() -> new GlobalException("CLM-004", docId));
        ClaimDocumentStatus status = request.getDocumentStatus() != null
                ? request.getDocumentStatus() : ClaimDocumentStatus.VERIFIED;
        doc.setDocumentStatus(status);
        if (request.getRemarks() != null) {
            doc.setRemarks(request.getRemarks());
        }
        doc = documentRepository.save(doc);

        ClaimStatus from = claim.getClaimStatus();
        if (!hasMissingRequiredDocs(id) && (from == ClaimStatus.DOCS_PENDING || from == ClaimStatus.REGISTERED)) {
            claim.setClaimStatus(ClaimStatus.UNDER_VERIFICATION);
            claimRepository.save(claim);
            appendEvent(claim, "DOCS_COMPLETE", from.name(), ClaimStatus.UNDER_VERIFICATION.name(),
                    "All required documents verified", null);
        } else {
            appendEvent(claim, "DOCUMENT_VERIFIED", from.name(), from.name(),
                    "Document " + doc.getDocumentType() + " -> " + status, null);
        }
        return toDocumentDto(doc);
    }

    @Override
    @Transactional
    public ClaimInvestigationDto investigate(UUID id, ClaimInvestigationRequest request) {
        Claim claim = findActive(id);
        ClaimStatus from = claim.getClaimStatus();
        ClaimInvestigation inv = ClaimInvestigation.builder()
                .claim(claim)
                .assignedTo(request.getAssignedTo())
                .dueDate(request.getDueDate())
                .notes(request.getNotes())
                .outcome(request.getOutcome())
                .completedAt(request.getOutcome() != null ? LocalDateTime.now() : null)
                .build();
        inv = investigationRepository.save(inv);
        claim.setClaimStatus(ClaimStatus.UNDER_INVESTIGATION);
        claimRepository.save(claim);
        appendEvent(claim, "INVESTIGATION", from.name(), ClaimStatus.UNDER_INVESTIGATION.name(),
                "Investigation assigned to " + request.getAssignedTo(), request.getAssignedTo());
        return toInvestigationDto(inv);
    }

    @Override
    @Transactional
    public ClaimFraudDto fraud(UUID id, ClaimFraudRequest request) {
        Claim claim = findActive(id);
        ClaimFraudCase fraud = ClaimFraudCase.builder()
                .claim(claim)
                .score(request.getScore())
                .indicators(request.getIndicators())
                .disposition(request.getDisposition() != null ? request.getDisposition() : FraudDisposition.UNDER_REVIEW)
                .notes(request.getNotes())
                .reviewedBy(request.getReviewedBy())
                .reviewedAt(LocalDateTime.now())
                .build();
        fraud = fraudCaseRepository.save(fraud);
        if (fraud.getDisposition() == FraudDisposition.CONFIRMED) {
            claim.setFraudFlag(true);
            claimRepository.save(claim);
        }
        appendEvent(claim, "FRAUD", claim.getClaimStatus().name(), claim.getClaimStatus().name(),
                "Fraud case disposition=" + fraud.getDisposition(), request.getReviewedBy());
        return toFraudDto(fraud);
    }

    @Override
    @Transactional
    public ClaimSurveyDto survey(UUID id, ClaimSurveyRequest request) {
        Claim claim = findActive(id);
        ClaimStatus from = claim.getClaimStatus();
        LocalDateTime now = LocalDateTime.now();
        ClaimSurvey survey = ClaimSurvey.builder()
                .claim(claim)
                .surveyorName(request.getSurveyorName())
                .surveyorId(request.getSurveyorId())
                .scheduledAt(request.getScheduledAt())
                .assessedLoss(request.getAssessedLoss())
                .reportDocumentId(request.getReportDocumentId())
                .notes(request.getNotes())
                .completedAt(Boolean.TRUE.equals(request.getCompleted()) ? now : null)
                .build();
        survey = surveyRepository.save(survey);
        claim.setClaimStatus(ClaimStatus.SURVEY_PENDING);
        claimRepository.save(claim);
        appendEvent(claim, "SURVEY", from.name(), ClaimStatus.SURVEY_PENDING.name(),
                "Survey assigned to " + request.getSurveyorName(), null);
        return toSurveyDto(survey);
    }

    @Override
    @Transactional
    public ClaimMedicalReviewDto medicalReview(UUID id, ClaimMedicalReviewRequest request) {
        Claim claim = findActive(id);
        ClaimStatus from = claim.getClaimStatus();
        ClaimMedicalReview review = ClaimMedicalReview.builder()
                .claim(claim)
                .hospitalId(request.getHospitalId())
                .hospitalName(request.getHospitalName())
                .diagnosisCodes(request.getDiagnosisCodes())
                .billedAmount(request.getBilledAmount())
                .approvedAmount(request.getApprovedAmount())
                .approvedDays(request.getApprovedDays())
                .cashless(request.getCashless() != null ? request.getCashless() : false)
                .reviewer(request.getReviewer())
                .reviewNotes(request.getReviewNotes())
                .reviewedAt(LocalDateTime.now())
                .build();
        review = medicalReviewRepository.save(review);
        claim.setClaimStatus(ClaimStatus.MEDICAL_REVIEW);
        claimRepository.save(claim);
        appendEvent(claim, "MEDICAL_REVIEW", from.name(), ClaimStatus.MEDICAL_REVIEW.name(),
                "Medical review completed", request.getReviewer());
        return toMedicalDto(review);
    }

    @Override
    @Transactional
    public ClaimDto approve(UUID id, ClaimDecisionRequest request) {
        Claim claim = findActive(id);
        BigDecimal amount = request.getAmount() != null ? request.getAmount()
                : (claim.getEstimatedLoss() != null ? claim.getEstimatedLoss() : BigDecimal.ZERO);

        boolean needsChecker = amount.compareTo(CHECKER_THRESHOLD) > 0;
        String authority = request.getAuthorityLevel() != null ? request.getAuthorityLevel().toUpperCase(Locale.ROOT) : "";
        if (needsChecker && !"CHECKER".equals(authority) && !StringUtils.hasText(request.getCheckerBy())) {
            throw new GlobalException("CLM-010", amount);
        }

        ClaimStatus from = claim.getClaimStatus();
        LocalDateTime now = LocalDateTime.now();
        ClaimDecision decision = ClaimDecision.builder()
                .claim(claim)
                .decisionType(request.getDecisionType() != null ? request.getDecisionType() : ClaimDecisionType.APPROVE)
                .amount(amount)
                .authorityLevel(request.getAuthorityLevel())
                .remarks(request.getRemarks())
                .decidedBy(request.getDecidedBy())
                .decidedAt(now)
                .checkerRequired(needsChecker)
                .checkerBy(request.getCheckerBy())
                .checkerAt(StringUtils.hasText(request.getCheckerBy()) ? now : null)
                .build();
        decisionRepository.save(decision);

        claim.setApprovedAmount(amount);
        claim.setClaimStatus(ClaimStatus.APPROVED);
        claim = claimRepository.save(claim);
        appendEvent(claim, "APPROVED", from.name(), ClaimStatus.APPROVED.name(),
                "Claim approved for " + amount, request.getDecidedBy());
        return toDto(claim, true);
    }

    @Override
    @Transactional
    public ClaimDto reject(UUID id, ClaimDecisionRequest request) {
        Claim claim = findActive(id);
        ClaimStatus from = claim.getClaimStatus();
        LocalDateTime now = LocalDateTime.now();
        ClaimDecision decision = ClaimDecision.builder()
                .claim(claim)
                .decisionType(ClaimDecisionType.REJECT)
                .amount(request.getAmount())
                .authorityLevel(request.getAuthorityLevel())
                .remarks(request.getRemarks())
                .decidedBy(request.getDecidedBy())
                .decidedAt(now)
                .checkerRequired(false)
                .build();
        decisionRepository.save(decision);
        claim.setClaimStatus(ClaimStatus.REJECTED);
        claim = claimRepository.save(claim);
        appendEvent(claim, "REJECTED", from.name(), ClaimStatus.REJECTED.name(),
                request.getRemarks() != null ? request.getRemarks() : "Claim rejected", request.getDecidedBy());
        return toDto(claim, true);
    }

    @Override
    @Transactional
    public ClaimDto settle(UUID id, ClaimSettlementRequest request) {
        Claim claim = findActive(id);
        if (claim.getClaimStatus() != ClaimStatus.APPROVED && claim.getClaimStatus() != ClaimStatus.SETTLED) {
            throw new GlobalException("CLM-006", claim.getClaimStatus());
        }
        ClaimStatus from = claim.getClaimStatus();
        BigDecimal gross = nz(request.getGrossAmount() != null ? request.getGrossAmount() : claim.getApprovedAmount());
        BigDecimal deductible = nz(request.getDeductibleAmount());
        BigDecimal depreciation = nz(request.getDepreciationAmount());
        BigDecimal betterment = nz(request.getBettermentAmount());
        BigDecimal unpaid = nz(request.getUnpaidPremiumOffset());
        BigDecimal recovery = nz(request.getRecoveryReserve());
        BigDecimal tax = nz(request.getTaxAmount());
        BigDecimal net = request.getNetAmount();
        if (net == null) {
            net = gross.subtract(deductible).subtract(depreciation).subtract(betterment)
                    .subtract(unpaid).subtract(recovery).subtract(tax);
        }

        LocalDateTime now = LocalDateTime.now();
        ClaimSettlement settlement = ClaimSettlement.builder()
                .claim(claim)
                .grossAmount(gross)
                .deductibleAmount(deductible)
                .depreciationAmount(depreciation)
                .bettermentAmount(betterment)
                .unpaidPremiumOffset(unpaid)
                .recoveryReserve(recovery)
                .taxAmount(tax)
                .netAmount(net)
                .paymentMethod(request.getPaymentMethod())
                .remarks(request.getRemarks())
                .settledAt(now)
                .settledBy(request.getSettledBy())
                .build();
        settlementRepository.save(settlement);

        claim.setSettledAmount(net);
        claim.setClaimStatus(ClaimStatus.SETTLED);
        claim = claimRepository.save(claim);
        appendEvent(claim, "SETTLED", from.name(), ClaimStatus.SETTLED.name(),
                "Settled net=" + net, request.getSettledBy());
        return toDto(claim, true);
    }

    @Override
    @Transactional
    public ClaimPaymentDto addPayment(UUID id, ClaimPaymentRequest request) {
        Claim claim = findActive(id);
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new GlobalException("CLM-014");
        }
        PaymentStatus payStatus = request.getPaymentStatus() != null ? request.getPaymentStatus() : PaymentStatus.PENDING;
        LocalDateTime now = LocalDateTime.now();
        ClaimPayment payment = ClaimPayment.builder()
                .claim(claim)
                .amount(request.getAmount())
                .payeeName(request.getPayeeName())
                .paymentRef(request.getPaymentRef())
                .utr(request.getUtr())
                .paymentStatus(payStatus)
                .paidAt(payStatus == PaymentStatus.PAID ? now : null)
                .billingPaymentId(request.getBillingPaymentId())
                .remarks(request.getRemarks())
                .build();
        payment = paymentRepository.save(payment);

        ClaimStatus from = claim.getClaimStatus();
        if (payStatus == PaymentStatus.PAID) {
            BigDecimal paid = nz(claim.getPaidAmount()).add(request.getAmount());
            claim.setPaidAmount(paid);
            claim.setClaimStatus(ClaimStatus.PAID);
            BigDecimal settled = nz(claim.getSettledAmount());
            if (settled.compareTo(BigDecimal.ZERO) > 0 && paid.compareTo(settled) >= 0) {
                claim.setClaimStatus(ClaimStatus.CLOSED);
            }
            claimRepository.save(claim);
            appendEvent(claim, "PAYMENT", from.name(), claim.getClaimStatus().name(),
                    "Payment of " + request.getAmount() + " recorded", null);
        } else {
            appendEvent(claim, "PAYMENT", from.name(), from.name(),
                    "Payment recorded status=" + payStatus, null);
        }
        return toPaymentDto(payment);
    }

    @Override
    @Transactional
    public ClaimRecoveryDto addRecovery(UUID id, ClaimRecoveryRequest request) {
        Claim claim = findActive(id);
        ClaimRecovery recovery = ClaimRecovery.builder()
                .claim(claim)
                .source(request.getSource())
                .amount(request.getAmount())
                .recoveryStatus(request.getRecoveryStatus() != null ? request.getRecoveryStatus() : RecoveryStatus.OPEN)
                .collectedAt(request.getRecoveryStatus() == RecoveryStatus.COLLECTED ? LocalDateTime.now() : null)
                .remarks(request.getRemarks())
                .build();
        recovery = recoveryRepository.save(recovery);
        appendEvent(claim, "RECOVERY", claim.getClaimStatus().name(), claim.getClaimStatus().name(),
                "Recovery added: " + request.getSource(), null);
        return toRecoveryDto(recovery);
    }

    @Override
    @Transactional
    public ClaimSalvageDto addSalvage(UUID id, ClaimSalvageRequest request) {
        Claim claim = findActive(id);
        ClaimSalvage salvage = ClaimSalvage.builder()
                .claim(claim)
                .itemDescription(request.getItemDescription())
                .estimatedValue(request.getEstimatedValue())
                .realizedValue(request.getRealizedValue())
                .salvageStatus(request.getSalvageStatus() != null ? request.getSalvageStatus() : SalvageStatus.OPEN)
                .disposedAt(request.getSalvageStatus() == SalvageStatus.DISPOSED
                        || request.getSalvageStatus() == SalvageStatus.SOLD ? LocalDateTime.now() : null)
                .remarks(request.getRemarks())
                .build();
        salvage = salvageRepository.save(salvage);
        appendEvent(claim, "SALVAGE", claim.getClaimStatus().name(), claim.getClaimStatus().name(),
                "Salvage added", null);
        return toSalvageDto(salvage);
    }

    @Override
    @Transactional
    public ClaimReserveDto reviseReserve(UUID id, ClaimReserveRequest request) {
        Claim claim = findActive(id);
        BigDecimal previous = claim.getReserveAmount();
        ClaimReserve reserve = ClaimReserve.builder()
                .claim(claim)
                .amount(request.getAmount())
                .previousAmount(previous)
                .reason(request.getReason())
                .revisedBy(request.getRevisedBy())
                .revisedAt(LocalDateTime.now())
                .build();
        reserve = reserveRepository.save(reserve);
        claim.setReserveAmount(request.getAmount());
        claimRepository.save(claim);
        appendEvent(claim, "RESERVE", claim.getClaimStatus().name(), claim.getClaimStatus().name(),
                "Reserve revised to " + request.getAmount(), request.getRevisedBy());
        return toReserveDto(reserve);
    }

    @Override
    @Transactional
    public ClaimDto reopen(UUID id, ClaimReopenRequest request) {
        Claim claim = findActive(id);
        if (claim.getClaimStatus() != ClaimStatus.CLOSED && claim.getClaimStatus() != ClaimStatus.REJECTED) {
            throw new GlobalException("CLM-009", claim.getClaimStatus());
        }
        ClaimStatus from = claim.getClaimStatus();
        claim.setClaimStatus(ClaimStatus.REOPENED);
        claimRepository.save(claim);
        appendEvent(claim, "REOPENED", from.name(), ClaimStatus.REOPENED.name(),
                request.getReason() != null ? request.getReason() : "Claim reopened", null);
        claim.setClaimStatus(ClaimStatus.UNDER_VERIFICATION);
        claim = claimRepository.save(claim);
        appendEvent(claim, "STATUS", ClaimStatus.REOPENED.name(), ClaimStatus.UNDER_VERIFICATION.name(),
                "Moved to under verification after reopen", null);
        return toDto(claim, true);
    }

    @Override
    @Transactional
    public ClaimDto hold(UUID id, ClaimHoldRequest request) {
        Claim claim = findActive(id);
        if (claim.getClaimStatus() == ClaimStatus.ON_HOLD) {
            throw new GlobalException("CLM-011");
        }
        if (claim.getClaimStatus() == ClaimStatus.CLOSED || claim.getClaimStatus() == ClaimStatus.PAID) {
            throw new GlobalException("CLM-011", claim.getClaimStatus());
        }
        ClaimStatus from = claim.getClaimStatus();
        claim.setOnHoldReason(request.getReason());
        claim.setClaimStatus(ClaimStatus.ON_HOLD);
        claim = claimRepository.save(claim);
        appendEvent(claim, "HOLD", from.name(), ClaimStatus.ON_HOLD.name(),
                request.getReason(), null);
        return toDto(claim, true);
    }

    @Override
    @Transactional
    public ClaimDto resume(UUID id) {
        Claim claim = findActive(id);
        if (claim.getClaimStatus() != ClaimStatus.ON_HOLD) {
            throw new GlobalException("CLM-012", claim.getClaimStatus());
        }
        ClaimStatus from = claim.getClaimStatus();
        claim.setOnHoldReason(null);
        claim.setClaimStatus(ClaimStatus.UNDER_VERIFICATION);
        claim = claimRepository.save(claim);
        appendEvent(claim, "RESUME", from.name(), ClaimStatus.UNDER_VERIFICATION.name(),
                "Claim resumed from hold", null);
        return toDto(claim, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaimEventDto> getEvents(UUID id) {
        findActive(id);
        return eventRepository.findByClaimIdOrderByOccurredAtDesc(id).stream()
                .map(this::toEventDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaimChecklistTemplateDto> listChecklistTemplates() {
        return checklistTemplateRepository.findAll().stream()
                .map(t -> ClaimChecklistTemplateDto.builder()
                        .id(t.getId())
                        .claimType(t.getClaimType())
                        .documentType(t.getDocumentType())
                        .documentName(t.getDocumentName())
                        .required(t.getRequired())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClaimDashboardSummaryDto getDashboardSummary() {
        long openCount = claimRepository.countByClaimStatusInAndDeletedFalse(OPEN_STATUSES);
        long pendingApproval = claimRepository.countByClaimStatusAndDeletedFalse(ClaimStatus.PENDING_APPROVAL);

        LocalDateTime yearStart = LocalDate.now().withDayOfYear(1).atStartOfDay();
        BigDecimal paidYtd = claimRepository.sumPaidAmountYtd(yearStart,
                EnumSet.of(ClaimStatus.PAID, ClaimStatus.CLOSED, ClaimStatus.SETTLED));
        if (paidYtd == null) paidYtd = BigDecimal.ZERO;

        List<Claim> settled = claimRepository.findSettledForAvgDays(
                EnumSet.of(ClaimStatus.SETTLED, ClaimStatus.PAID, ClaimStatus.CLOSED));
        double avgDays = 0;
        if (!settled.isEmpty()) {
            double sum = 0;
            int n = 0;
            for (Claim c : settled) {
                if (c.getIntimatedAt() != null && c.getUpdatedAt() != null) {
                    sum += ChronoUnit.DAYS.between(c.getIntimatedAt().toLocalDate(), c.getUpdatedAt().toLocalDate());
                    n++;
                }
            }
            avgDays = n > 0 ? BigDecimal.valueOf(sum / n).setScale(1, RoundingMode.HALF_UP).doubleValue() : 0;
        }

        long fraudOpen = fraudCaseRepository.countByDispositionAndClaimNotDeleted(FraudDisposition.UNDER_REVIEW)
                + fraudCaseRepository.countByDispositionAndClaimNotDeleted(FraudDisposition.CONFIRMED);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime d7 = now.minusDays(7);
        LocalDateTime d30 = now.minusDays(30);
        long aging0to7 = claimRepository.countByDeletedFalseAndClaimStatusInAndCreatedAtBetween(OPEN_STATUSES, d7, now);
        long aging8to30 = claimRepository.countByDeletedFalseAndClaimStatusInAndCreatedAtBetween(OPEN_STATUSES, d30, d7);
        long aging31plus = claimRepository.countByDeletedFalseAndClaimStatusInAndCreatedAtBefore(OPEN_STATUSES, d30);

        Map<String, Long> buckets = new LinkedHashMap<>();
        buckets.put("aging0to7", aging0to7);
        buckets.put("aging8to30", aging8to30);
        buckets.put("aging31plus", aging31plus);

        return ClaimDashboardSummaryDto.builder()
                .openCount(openCount)
                .pendingApprovalCount(pendingApproval)
                .paidYtdAmount(paidYtd)
                .avgSettlementDays(avgDays)
                .fraudOpenCount(fraudOpen)
                .aging0to7(aging0to7)
                .aging8to30(aging8to30)
                .aging31plus(aging31plus)
                .agingBuckets(buckets)
                .build();
    }

    // ---- helpers ----

    private void appendEvent(Claim claim, String type, String from, String to, String message, String actor) {
        ClaimEvent event = ClaimEvent.builder()
                .claim(claim)
                .eventType(type)
                .fromStatus(from)
                .toStatus(to)
                .message(message)
                .actor(actor)
                .occurredAt(LocalDateTime.now())
                .build();
        eventRepository.save(event);
    }

    private String generateClaimNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String prefix = "CLM-" + datePart + "-";
        long count = claimRepository.countByDeletedFalseAndClaimNumberStartingWith(prefix);
        return prefix + String.format("%04d", count + 1);
    }

    private Claim findActive(UUID id) {
        return claimRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new GlobalException("CLM-001", id));
    }

    private boolean hasMissingRequiredDocs(UUID claimId) {
        return documentRepository.findByClaimId(claimId).stream()
                .anyMatch(d -> Boolean.TRUE.equals(d.getRequired())
                        && d.getDocumentStatus() != ClaimDocumentStatus.VERIFIED);
    }

    private Specification<Claim> buildSearchSpec(ClaimSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));
            if (StringUtils.hasText(request.getClaimNumber())) {
                predicates.add(cb.like(cb.lower(root.get("claimNumber")),
                        "%" + request.getClaimNumber().toLowerCase(Locale.ROOT) + "%"));
            }
            if (StringUtils.hasText(request.getPolicyNumber())) {
                predicates.add(cb.like(cb.lower(root.get("policyNumber")),
                        "%" + request.getPolicyNumber().toLowerCase(Locale.ROOT) + "%"));
            }
            if (request.getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customerId"), request.getCustomerId()));
            }
            if (request.getClaimStatus() != null) {
                predicates.add(cb.equal(root.get("claimStatus"), request.getClaimStatus()));
            }
            if (request.getClaimType() != null) {
                predicates.add(cb.equal(root.get("claimType"), request.getClaimType()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private BigDecimal nz(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }

    private ClaimDto toDto(Claim claim, boolean nested) {
        ClaimDto.ClaimDtoBuilder b = ClaimDto.builder()
                .id(claim.getId())
                .claimNumber(claim.getClaimNumber())
                .policyId(claim.getPolicyId())
                .policyNumber(claim.getPolicyNumber())
                .customerId(claim.getCustomerId())
                .customerName(claim.getCustomerName())
                .insuredPartyId(claim.getInsuredPartyId())
                .insuredPartyName(claim.getInsuredPartyName())
                .claimType(claim.getClaimType())
                .lossDate(claim.getLossDate())
                .lossTime(claim.getLossTime())
                .lossLocation(claim.getLossLocation())
                .causeOfLoss(claim.getCauseOfLoss())
                .description(claim.getDescription())
                .estimatedLoss(claim.getEstimatedLoss())
                .intimatedAt(claim.getIntimatedAt())
                .reportedBy(claim.getReportedBy())
                .branchId(claim.getBranchId())
                .claimStatus(claim.getClaimStatus())
                .reserveAmount(claim.getReserveAmount())
                .approvedAmount(claim.getApprovedAmount())
                .settledAmount(claim.getSettledAmount())
                .paidAmount(claim.getPaidAmount())
                .currency(claim.getCurrency())
                .priority(claim.getPriority())
                .fraudFlag(claim.getFraudFlag())
                .catastropheCode(claim.getCatastropheCode())
                .policeFirNumber(claim.getPoliceFirNumber())
                .hospitalAdmissionDate(claim.getHospitalAdmissionDate())
                .verificationNotes(claim.getVerificationNotes())
                .eligibilityPassed(claim.getEligibilityPassed())
                .onHoldReason(claim.getOnHoldReason())
                .litigationFlag(claim.getLitigationFlag())
                .reinsuranceShareAmount(claim.getReinsuranceShareAmount())
                .reinsuranceShareStatus(claim.getReinsuranceShareStatus())
                .createdAt(claim.getCreatedAt())
                .updatedAt(claim.getUpdatedAt());

        if (nested) {
            UUID id = claim.getId();
            b.coverages(coverageItemRepository.findByClaimId(id).stream().map(this::toCoverageDto).collect(Collectors.toList()));
            b.parties(partyRepository.findByClaimId(id).stream().map(this::toPartyDto).collect(Collectors.toList()));
            b.documents(documentRepository.findByClaimId(id).stream().map(this::toDocumentDto).collect(Collectors.toList()));
            b.latestInvestigation(investigationRepository.findFirstByClaimIdOrderByCreatedAtDesc(id).map(this::toInvestigationDto).orElse(null));
            b.latestFraud(fraudCaseRepository.findFirstByClaimIdOrderByCreatedAtDesc(id).map(this::toFraudDto).orElse(null));
            b.latestSurvey(surveyRepository.findFirstByClaimIdOrderByCreatedAtDesc(id).map(this::toSurveyDto).orElse(null));
            b.latestMedicalReview(medicalReviewRepository.findFirstByClaimIdOrderByCreatedAtDesc(id).map(this::toMedicalDto).orElse(null));
            b.settlements(settlementRepository.findByClaimIdOrderBySettledAtDesc(id).stream().map(this::toSettlementDto).collect(Collectors.toList()));
            b.payments(paymentRepository.findByClaimIdOrderByCreatedAtDesc(id).stream().map(this::toPaymentDto).collect(Collectors.toList()));
            b.recoveries(recoveryRepository.findByClaimIdOrderByCreatedAtDesc(id).stream().map(this::toRecoveryDto).collect(Collectors.toList()));
            b.salvages(salvageRepository.findByClaimIdOrderByCreatedAtDesc(id).stream().map(this::toSalvageDto).collect(Collectors.toList()));
            b.recentEvents(eventRepository.findTop20ByClaimIdOrderByOccurredAtDesc(id).stream().map(this::toEventDto).collect(Collectors.toList()));
        }
        return b.build();
    }

    private ClaimCoverageItemDto toCoverageDto(ClaimCoverageItem c) {
        return ClaimCoverageItemDto.builder()
                .id(c.getId()).coverageCode(c.getCoverageCode()).coverageName(c.getCoverageName())
                .sumInsured(c.getSumInsured()).claimedAmount(c.getClaimedAmount())
                .deductible(c.getDeductible()).coinsurancePercent(c.getCoinsurancePercent())
                .approvedAmount(c.getApprovedAmount()).build();
    }

    private ClaimPartyDto toPartyDto(ClaimParty p) {
        return ClaimPartyDto.builder()
                .id(p.getId()).role(p.getRole()).name(p.getName())
                .contactPhone(p.getContactPhone()).contactEmail(p.getContactEmail())
                .bankName(p.getBankName()).bankAccount(p.getBankAccount()).bankIfsc(p.getBankIfsc())
                .referenceId(p.getReferenceId()).build();
    }

    private ClaimDocumentDto toDocumentDto(ClaimDocument d) {
        return ClaimDocumentDto.builder()
                .id(d.getId()).documentType(d.getDocumentType()).documentName(d.getDocumentName())
                .documentId(d.getDocumentId()).documentStatus(d.getDocumentStatus())
                .remarks(d.getRemarks()).required(d.getRequired()).build();
    }

    private ClaimInvestigationDto toInvestigationDto(ClaimInvestigation i) {
        return ClaimInvestigationDto.builder()
                .id(i.getId()).assignedTo(i.getAssignedTo()).dueDate(i.getDueDate())
                .notes(i.getNotes()).outcome(i.getOutcome()).completedAt(i.getCompletedAt()).build();
    }

    private ClaimFraudDto toFraudDto(ClaimFraudCase f) {
        return ClaimFraudDto.builder()
                .id(f.getId()).score(f.getScore()).indicators(f.getIndicators())
                .disposition(f.getDisposition()).notes(f.getNotes())
                .reviewedBy(f.getReviewedBy()).reviewedAt(f.getReviewedAt()).build();
    }

    private ClaimSurveyDto toSurveyDto(ClaimSurvey s) {
        return ClaimSurveyDto.builder()
                .id(s.getId()).surveyorName(s.getSurveyorName()).surveyorId(s.getSurveyorId())
                .scheduledAt(s.getScheduledAt()).assessedLoss(s.getAssessedLoss())
                .reportDocumentId(s.getReportDocumentId()).notes(s.getNotes())
                .completedAt(s.getCompletedAt()).build();
    }

    private ClaimMedicalReviewDto toMedicalDto(ClaimMedicalReview m) {
        return ClaimMedicalReviewDto.builder()
                .id(m.getId()).hospitalId(m.getHospitalId()).hospitalName(m.getHospitalName())
                .diagnosisCodes(m.getDiagnosisCodes()).billedAmount(m.getBilledAmount())
                .approvedAmount(m.getApprovedAmount()).approvedDays(m.getApprovedDays())
                .cashless(m.getCashless()).reviewer(m.getReviewer())
                .reviewNotes(m.getReviewNotes()).reviewedAt(m.getReviewedAt()).build();
    }

    private ClaimSettlementDto toSettlementDto(ClaimSettlement s) {
        return ClaimSettlementDto.builder()
                .id(s.getId()).grossAmount(s.getGrossAmount()).deductibleAmount(s.getDeductibleAmount())
                .depreciationAmount(s.getDepreciationAmount()).bettermentAmount(s.getBettermentAmount())
                .unpaidPremiumOffset(s.getUnpaidPremiumOffset()).recoveryReserve(s.getRecoveryReserve())
                .taxAmount(s.getTaxAmount()).netAmount(s.getNetAmount())
                .paymentMethod(s.getPaymentMethod()).remarks(s.getRemarks())
                .settledAt(s.getSettledAt()).settledBy(s.getSettledBy()).build();
    }

    private ClaimPaymentDto toPaymentDto(ClaimPayment p) {
        return ClaimPaymentDto.builder()
                .id(p.getId()).amount(p.getAmount()).payeeName(p.getPayeeName())
                .paymentRef(p.getPaymentRef()).utr(p.getUtr()).paymentStatus(p.getPaymentStatus())
                .paidAt(p.getPaidAt()).billingPaymentId(p.getBillingPaymentId())
                .remarks(p.getRemarks()).build();
    }

    private ClaimRecoveryDto toRecoveryDto(ClaimRecovery r) {
        return ClaimRecoveryDto.builder()
                .id(r.getId()).source(r.getSource()).amount(r.getAmount())
                .recoveryStatus(r.getRecoveryStatus()).collectedAt(r.getCollectedAt())
                .remarks(r.getRemarks()).build();
    }

    private ClaimSalvageDto toSalvageDto(ClaimSalvage s) {
        return ClaimSalvageDto.builder()
                .id(s.getId()).itemDescription(s.getItemDescription())
                .estimatedValue(s.getEstimatedValue()).realizedValue(s.getRealizedValue())
                .salvageStatus(s.getSalvageStatus()).disposedAt(s.getDisposedAt())
                .remarks(s.getRemarks()).build();
    }

    private ClaimReserveDto toReserveDto(ClaimReserve r) {
        return ClaimReserveDto.builder()
                .id(r.getId()).amount(r.getAmount()).previousAmount(r.getPreviousAmount())
                .reason(r.getReason()).revisedBy(r.getRevisedBy()).revisedAt(r.getRevisedAt()).build();
    }

    private ClaimEventDto toEventDto(ClaimEvent e) {
        return ClaimEventDto.builder()
                .id(e.getId()).eventType(e.getEventType()).fromStatus(e.getFromStatus())
                .toStatus(e.getToStatus()).message(e.getMessage()).actor(e.getActor())
                .occurredAt(e.getOccurredAt()).build();
    }
}
