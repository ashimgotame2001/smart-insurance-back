package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.applicationConfig.model.enums.CompanyStatus;
import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.commonService.model.Document;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "company_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyProfileEntity extends BaseEntity {

    @Column(name = "company_code", unique = true)
    private String companyCode;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_short_name")
    private String companyShortName;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "tax_identification_number")
    private String taxIdentificationNumber;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "support_email")
    private String supportEmail;

    @Column(name = "support_phone")
    private String supportPhone;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "logo_url")
    private String logoUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logo_id")
    private Document logo;

    @Column(name = "favicon_url")
    private String faviconUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favicon_id")
    private Document favicon;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "secondary_color")
    private String secondaryColor;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "currency_format")
    private String currencyFormat;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "company_status")
    @Enumerated(EnumType.STRING)
    private CompanyStatus companyStatus;

    @Column(name = "established_date")
    private LocalDate establishedDate;

    @Column(name = "remarks")
    private String remarks;

}
