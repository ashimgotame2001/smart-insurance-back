package com.project.smartinsurance.claimsService.service;

import com.project.smartinsurance.claimsService.dto.*;
import com.project.smartinsurance.commonService.dto.PagedData;

import java.util.List;
import java.util.UUID;

public interface ClaimService {

    ClaimDto registerClaim(ClaimCreateRequest request);

    ClaimDto updateClaim(UUID id, ClaimUpdateRequest request);

    ClaimDto getById(UUID id);

    ClaimDto getByClaimNumber(String claimNumber);

    PagedData<ClaimDto> search(ClaimSearchRequest request);

    PagedData<ClaimDto> list(int page, int size, String sortBy, String sortDir);

    void softDelete(UUID id);

    ClaimDto verify(UUID id, ClaimVerifyRequest request);

    ClaimDocumentDto addDocument(UUID id, ClaimDocumentRequest request);

    ClaimDocumentDto verifyDocument(UUID id, UUID docId, ClaimDocumentVerifyRequest request);

    ClaimInvestigationDto investigate(UUID id, ClaimInvestigationRequest request);

    ClaimFraudDto fraud(UUID id, ClaimFraudRequest request);

    ClaimSurveyDto survey(UUID id, ClaimSurveyRequest request);

    ClaimMedicalReviewDto medicalReview(UUID id, ClaimMedicalReviewRequest request);

    ClaimDto approve(UUID id, ClaimDecisionRequest request);

    ClaimDto reject(UUID id, ClaimDecisionRequest request);

    ClaimDto settle(UUID id, ClaimSettlementRequest request);

    ClaimPaymentDto addPayment(UUID id, ClaimPaymentRequest request);

    ClaimRecoveryDto addRecovery(UUID id, ClaimRecoveryRequest request);

    ClaimSalvageDto addSalvage(UUID id, ClaimSalvageRequest request);

    ClaimReserveDto reviseReserve(UUID id, ClaimReserveRequest request);

    ClaimDto reopen(UUID id, ClaimReopenRequest request);

    ClaimDto hold(UUID id, ClaimHoldRequest request);

    ClaimDto resume(UUID id);

    List<ClaimEventDto> getEvents(UUID id);

    List<ClaimChecklistTemplateDto> listChecklistTemplates();

    ClaimDashboardSummaryDto getDashboardSummary();
}
