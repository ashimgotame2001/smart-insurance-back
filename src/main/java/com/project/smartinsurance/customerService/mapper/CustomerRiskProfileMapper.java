package com.project.smartinsurance.customerService.mapper;

import com.project.smartinsurance.customerService.dto.CustomerRiskProfileDto;
import com.project.smartinsurance.customerService.model.CustomerRiskProfile;
import org.springframework.stereotype.Component;

@Component
public class CustomerRiskProfileMapper {

    public CustomerRiskProfileDto toDto(CustomerRiskProfile profile) {
        if (profile == null) return null;
        return CustomerRiskProfileDto.builder()
                .id(profile.getId())
                .status(profile.getStatus())
                .customerId(profile.getCustomer() != null ? profile.getCustomer().getId() : null)
                .customerCode(profile.getCustomer() != null ? profile.getCustomer().getCustomerCode() : null)
                .customerName(getCustomerName(profile))
                .riskCategory(profile.getRiskCategory() != null ? profile.getRiskCategory().name() : null)
                .riskScore(profile.getRiskScore())
                .assessmentDate(profile.getAssessmentDate())
                .assessedById(profile.getAssessedBy() != null ? profile.getAssessedBy().getId() : null)
                .assessedByName(profile.getAssessedBy() != null ? profile.getAssessedBy().getFullName() : null)
                .factors(profile.getFactors())
                .notes(profile.getNotes())
                .nextReviewDate(profile.getNextReviewDate())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    private String getCustomerName(CustomerRiskProfile profile) {
        if (profile.getCustomer() == null) return null;
        var c = profile.getCustomer();
        if (c instanceof com.project.smartinsurance.customerService.model.IndividualCustomer ic) {
            return (ic.getFirstName() != null ? ic.getFirstName() : "") + " " + (ic.getLastName() != null ? ic.getLastName() : "");
        }
        if (c instanceof com.project.smartinsurance.customerService.model.CorporateCustomer cc) {
            return cc.getCompanyName();
        }
        if (c instanceof com.project.smartinsurance.customerService.model.GovernmentCustomer gc) {
            return gc.getDepartmentName();
        }
        return c.getCustomerCode();
    }
}
