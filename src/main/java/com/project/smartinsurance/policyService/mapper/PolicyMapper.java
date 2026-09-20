package com.project.smartinsurance.policyService.mapper;

import com.project.smartinsurance.policyService.dto.*;
import com.project.smartinsurance.policyService.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PolicyMapper {

    @Mapping(target = "channel", expression = "java(policy.getChannel() != null ? policy.getChannel().name() : null)")
    @Mapping(target = "policyStatus", expression = "java(policy.getPolicyStatus() != null ? policy.getPolicyStatus().name() : null)")
    public abstract PolicyDto toDto(Policy policy);

    @Mapping(target = "status", expression = "java(coverage.getCoverageStatus() != null ? coverage.getCoverageStatus().name() : null)")
    public abstract CoverageDto toCoverageDto(Coverage coverage);

    public abstract PolicyPremiumDto toPremiumDto(PolicyPremium premium);

    @Mapping(target = "endorsementType", expression = "java(endorsement.getEndorsementType() != null ? endorsement.getEndorsementType().name() : null)")
    @Mapping(target = "status", expression = "java(endorsement.getEndorsementStatus() != null ? endorsement.getEndorsementStatus().name() : null)")
    public abstract EndorsementDto toEndorsementDto(Endorsement endorsement);

    public abstract PolicyVersionDto toVersionDto(PolicyVersion version);

    public abstract PolicyAuditEntryDto toAuditDto(PolicyAuditTrail audit);

    public void updateEntity(Policy policy, PolicyUpdateRequest request) {
        if (request.getProductCode() != null) {
            policy.setProductCode(request.getProductCode());
        }
        if (request.getPlanCode() != null) {
            policy.setPlanCode(request.getPlanCode());
        }
        if (request.getCustomerName() != null) {
            policy.setCustomerName(request.getCustomerName());
        }
        if (request.getInsuredPartyId() != null) {
            policy.setInsuredPartyId(request.getInsuredPartyId());
        }
        if (request.getInsuredPartyName() != null) {
            policy.setInsuredPartyName(request.getInsuredPartyName());
        }
        if (request.getInsuredPartyType() != null) {
            policy.setInsuredPartyType(request.getInsuredPartyType());
        }
        if (request.getChannel() != null) {
            policy.setChannel(com.project.smartinsurance.policyService.model.enums.PolicyChannel.valueOf(request.getChannel()));
        }
        if (request.getBranchId() != null) {
            policy.setBranchId(request.getBranchId());
        }
        if (request.getInceptionDate() != null) {
            policy.setInceptionDate(request.getInceptionDate());
        }
        if (request.getEffectiveDate() != null) {
            policy.setEffectiveDate(request.getEffectiveDate());
        }
        if (request.getExpiryDate() != null) {
            policy.setExpiryDate(request.getExpiryDate());
        }
        if (request.getIssueDate() != null) {
            policy.setIssueDate(request.getIssueDate());
        }
        if (request.getTotalSumInsured() != null) {
            policy.setTotalSumInsured(request.getTotalSumInsured());
        }
        if (request.getBasePremium() != null) {
            policy.setBasePremium(request.getBasePremium());
        }
        if (request.getTotalPremium() != null) {
            policy.setTotalPremium(request.getTotalPremium());
        }
        if (request.getCurrencyCode() != null) {
            policy.setCurrencyCode(request.getCurrencyCode());
        }
        if (request.getPaymentFrequency() != null) {
            policy.setPaymentFrequency(request.getPaymentFrequency());
        }
    }
}
