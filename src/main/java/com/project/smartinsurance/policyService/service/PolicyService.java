package com.project.smartinsurance.policyService.service;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.policyService.dto.*;

import java.util.List;
import java.util.UUID;

public interface PolicyService {

    PolicyDto createPolicy(PolicyCreateRequest request);

    PolicyDto updatePolicy(UUID id, PolicyUpdateRequest request);

    void deletePolicy(UUID id);

    PolicyDto getPolicyById(UUID id);

    PolicyDto getPolicyByNumber(String policyNumber);

    PagedData<PolicyDto> searchPolicies(PolicySearchRequest request);

    PagedData<PolicyDto> getAllPolicies(int page, int size, String sortBy, String sortDir);

    PagedData<PolicyDto> getPoliciesByCustomer(UUID customerId, int page, int size, String sortBy, String sortDir);

    PagedData<PolicyDto> getPoliciesByStatus(String status, int page, int size, String sortBy, String sortDir);

    PolicyDto cancelPolicy(UUID id, CancelPolicyRequest request);

    PolicyDto reinstatePolicy(UUID id, ReinstatePolicyRequest request);

    PolicyDto issuePolicy(UUID id);

    PolicyDto suspendPolicy(UUID id, CancelPolicyRequest request);

    PolicyDto transferPolicy(UUID id, TransferPolicyRequest request);

    PolicyDto renewPolicy(UUID id, RenewPolicyRequest request);

    PolicyDto upgradePolicy(UUID id, UpgradePolicyRequest request);

    PolicyDto downgradePolicy(UUID id, DowngradePolicyRequest request);

    PolicyDto approvePolicy(UUID id, String approvedBy);

    EndorsementDto createEndorsement(UUID policyId, EndorsementRequest request);

    EndorsementDto approveEndorsement(UUID endorsementId, String approvedBy);

    EndorsementDto rejectEndorsement(UUID endorsementId, String rejectedBy, String reason);

    List<EndorsementDto> getEndorsementsByPolicy(UUID policyId);

    List<CoverageDto> getCoveragesByPolicy(UUID policyId);

    List<PolicyPremiumDto> getPremiumsByPolicy(UUID policyId);

    List<PolicyVersionDto> getVersionsByPolicy(UUID policyId);

    List<PolicyAuditEntryDto> getAuditTrail(String entityType, String entityId);

    PolicySummaryResponse getPolicySummary();
}
