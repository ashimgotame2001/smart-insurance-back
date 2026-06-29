package com.project.smartinsurance.customerService.model;

import com.project.smartinsurance.customerService.model.enums.GovernmentLevel;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("GOVERNMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GovernmentCustomer extends Customer {

    @Column(name = "ministry_name")
    private String ministryName;

    @Column(name = "department_name")
    private String departmentName;

    @Enumerated(EnumType.STRING)
    @Column(name = "government_level")
    private GovernmentLevel governmentLevel;

    @Column(name = "established_year")
    private LocalDate establishedYear;

    @Column(name = "budget_code")
    private String budgetCode;

    @Column(name = "funding_source")
    private String fundingSource;

    @Column(name = "department_head_name")
    private String departmentHeadName;

    @Column(name = "department_head_designation")
    private String departmentHeadDesignation;

    @Column(name = "department_head_phone")
    private String departmentHeadPhone;

    @Column(name = "department_head_email")
    private String departmentHeadEmail;

    @Column(name = "office_address", columnDefinition = "TEXT")
    private String officeAddress;
}
