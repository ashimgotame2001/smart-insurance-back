package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.dto.DocumentDto;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.applicationConfig.model.enums.CompanyStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyProfileDto {

    private UUID id;
    private Status status;
    private String companyCode;
    private String companyName;
    private String companyShortName;
    private String registrationNumber;
    private String taxIdentificationNumber;
    private String licenseNumber;
    private String websiteUrl;
    private String email;
    private String phoneNumber;
    private String supportEmail;
    private String supportPhone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String logoUrl;
    private DocumentDto logo;
    private String faviconUrl;
    private DocumentDto favicon;
    private String primaryColor;
    private String secondaryColor;
    private String currencyCode;
    private String currencyFormat;
    private String timezone;
    private String businessType;
    private CompanyStatus companyStatus;
    private LocalDate establishedDate;
    private String remarks;
}
