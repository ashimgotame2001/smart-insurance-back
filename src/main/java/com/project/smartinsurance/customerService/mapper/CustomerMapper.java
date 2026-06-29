package com.project.smartinsurance.customerService.mapper;

import com.project.smartinsurance.customerService.dto.CorporateCustomerDto;
import com.project.smartinsurance.customerService.dto.CustomerDto;
import com.project.smartinsurance.customerService.dto.GovernmentCustomerDto;
import com.project.smartinsurance.customerService.dto.IndividualCustomerDto;
import com.project.smartinsurance.customerService.dto.KYCDto;
import com.project.smartinsurance.customerService.model.CorporateCustomer;
import com.project.smartinsurance.customerService.model.Customer;
import com.project.smartinsurance.customerService.model.GovernmentCustomer;
import com.project.smartinsurance.customerService.model.IndividualCustomer;
import com.project.smartinsurance.customerService.model.KYC;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDto toDto(Customer customer) {
        if (customer instanceof IndividualCustomer ic) {
            return toIndividualDto(ic);
        } else if (customer instanceof CorporateCustomer cc) {
            return toCorporateDto(cc);
        } else if (customer instanceof GovernmentCustomer gc) {
            return toGovernmentDto(gc);
        }
        return baseDto(customer, CustomerDto.builder());
    }

    public IndividualCustomerDto toIndividualDto(IndividualCustomer c) {
        return IndividualCustomerDto.builder()
                .id(c.getId()).status(c.getStatus()).customerCode(c.getCustomerCode())
                .customerType(c.getCustomerType() != null ? c.getCustomerType().name() : "INDIVIDUAL")
                .email(c.getEmail()).phone(c.getPhone()).primaryAddress(c.getPrimaryAddress())
                .kycStatus(c.getKycStatus() != null ? c.getKycStatus().name() : null)
                .onboardingStatus(c.getOnboardingStatus() != null ? c.getOnboardingStatus().name() : null)
                .notes(c.getNotes())
                .firstName(c.getFirstName()).lastName(c.getLastName()).middleName(c.getMiddleName())
                .dateOfBirth(c.getDateOfBirth())
                .gender(c.getGender() != null ? c.getGender().name() : null)
                .maritalStatus(c.getMaritalStatus()).nationality(c.getNationality())
                .occupation(c.getOccupation()).identityType(c.getIdentityType())
                .identityNumber(c.getIdentityNumber()).identityIssueDate(c.getIdentityIssueDate())
                .identityExpiryDate(c.getIdentityExpiryDate())
                .identityFrontDocId(c.getIdentityFrontDoc() != null ? c.getIdentityFrontDoc().getId() : null)
                .identityFrontDocUrl(c.getIdentityFrontDoc() != null ? c.getIdentityFrontDoc().getUrl() : null)
                .identityBackDocId(c.getIdentityBackDoc() != null ? c.getIdentityBackDoc().getId() : null)
                .identityBackDocUrl(c.getIdentityBackDoc() != null ? c.getIdentityBackDoc().getUrl() : null)
                .fatherName(c.getFatherName())
                .motherName(c.getMotherName()).placeOfBirth(c.getPlaceOfBirth())
                .build();
    }

    public CorporateCustomerDto toCorporateDto(CorporateCustomer c) {
        return CorporateCustomerDto.builder()
                .id(c.getId()).status(c.getStatus()).customerCode(c.getCustomerCode())
                .customerType(c.getCustomerType() != null ? c.getCustomerType().name() : "CORPORATE")
                .email(c.getEmail()).phone(c.getPhone()).primaryAddress(c.getPrimaryAddress())
                .kycStatus(c.getKycStatus() != null ? c.getKycStatus().name() : null)
                .onboardingStatus(c.getOnboardingStatus() != null ? c.getOnboardingStatus().name() : null)
                .notes(c.getNotes())
                .companyName(c.getCompanyName()).registrationNumber(c.getRegistrationNumber())
                .taxId(c.getTaxId()).incorporationDate(c.getIncorporationDate())
                .companyType(c.getCompanyType()).industry(c.getIndustry())
                .numberOfEmployees(c.getNumberOfEmployees())
                .authorizedCapital(c.getAuthorizedCapital()).paidUpCapital(c.getPaidUpCapital())
                .registeredAddress(c.getRegisteredAddress())
                .contactPersonName(c.getContactPersonName())
                .contactPersonDesignation(c.getContactPersonDesignation())
                .contactPersonPhone(c.getContactPersonPhone())
                .contactPersonEmail(c.getContactPersonEmail())
                .build();
    }

    public GovernmentCustomerDto toGovernmentDto(GovernmentCustomer c) {
        return GovernmentCustomerDto.builder()
                .id(c.getId()).status(c.getStatus()).customerCode(c.getCustomerCode())
                .customerType(c.getCustomerType() != null ? c.getCustomerType().name() : "GOVERNMENT")
                .email(c.getEmail()).phone(c.getPhone()).primaryAddress(c.getPrimaryAddress())
                .kycStatus(c.getKycStatus() != null ? c.getKycStatus().name() : null)
                .onboardingStatus(c.getOnboardingStatus() != null ? c.getOnboardingStatus().name() : null)
                .notes(c.getNotes())
                .ministryName(c.getMinistryName()).departmentName(c.getDepartmentName())
                .governmentLevel(c.getGovernmentLevel() != null ? c.getGovernmentLevel().name() : null)
                .establishedYear(c.getEstablishedYear()).budgetCode(c.getBudgetCode())
                .fundingSource(c.getFundingSource())
                .departmentHeadName(c.getDepartmentHeadName())
                .departmentHeadDesignation(c.getDepartmentHeadDesignation())
                .departmentHeadPhone(c.getDepartmentHeadPhone())
                .departmentHeadEmail(c.getDepartmentHeadEmail())
                .officeAddress(c.getOfficeAddress())
                .build();
    }

    public KYCDto toKycDto(KYC kyc) {
        KYCDto.KYCDtoBuilder builder = KYCDto.builder()
                .id(kyc.getId()).status(kyc.getStatus())
                .customerId(kyc.getCustomer().getId())
                .documentType(kyc.getDocumentType() != null ? kyc.getDocumentType().getCode() : null)
                .documentNumber(kyc.getDocumentNumber())
                .frontDocumentId(kyc.getFrontDocument() != null ? kyc.getFrontDocument().getId() : null)
                .frontDocumentUrl(kyc.getFrontDocument() != null ? kyc.getFrontDocument().getUrl() : null)
                .backDocumentId(kyc.getBackDocument() != null ? kyc.getBackDocument().getId() : null)
                .backDocumentUrl(kyc.getBackDocument() != null ? kyc.getBackDocument().getUrl() : null)
                .expiryDate(kyc.getExpiryDate())
                .issueDate(kyc.getIssueDate())
                .verificationStatus(kyc.getVerificationStatus())
                .rejectionReason(kyc.getRejectionReason()).remarks(kyc.getRemarks())
                .verifiedAt(kyc.getVerifiedAt()).createdAt(kyc.getCreatedAt());
        if (kyc.getVerifiedBy() != null) {
            builder.verifiedById(kyc.getVerifiedBy().getId());
            builder.verifiedByName(kyc.getVerifiedBy().getFullName());
        }
        return builder.build();
    }

    private CustomerDto baseDto(Customer c, CustomerDto.CustomerDtoBuilder<?, ?> builder) {
        return builder
                .id(c.getId()).status(c.getStatus()).customerCode(c.getCustomerCode())
                .customerType(c.getCustomerType() != null ? c.getCustomerType().name() : null)
                .email(c.getEmail()).phone(c.getPhone()).primaryAddress(c.getPrimaryAddress())
                .kycStatus(c.getKycStatus() != null ? c.getKycStatus().name() : null)
                .onboardingStatus(c.getOnboardingStatus() != null ? c.getOnboardingStatus().name() : null)
                .notes(c.getNotes())
                .build();
    }
}
