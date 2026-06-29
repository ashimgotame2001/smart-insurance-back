package com.project.smartinsurance.customerService.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("CORPORATE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CorporateCustomer extends Customer {

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "incorporation_date")
    private LocalDate incorporationDate;

    @Column(name = "company_type")
    private String companyType;

    @Column(name = "industry")
    private String industry;

    @Column(name = "number_of_employees")
    private Integer numberOfEmployees;

    @Column(name = "authorized_capital")
    private BigDecimal authorizedCapital;

    @Column(name = "paid_up_capital")
    private BigDecimal paidUpCapital;

    @Column(name = "registered_address", columnDefinition = "TEXT")
    private String registeredAddress;

    @Column(name = "contact_person_name")
    private String contactPersonName;

    @Column(name = "contact_person_designation")
    private String contactPersonDesignation;

    @Column(name = "contact_person_phone")
    private String contactPersonPhone;

    @Column(name = "contact_person_email")
    private String contactPersonEmail;
}
