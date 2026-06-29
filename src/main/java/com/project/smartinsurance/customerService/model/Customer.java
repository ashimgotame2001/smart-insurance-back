package com.project.smartinsurance.customerService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.customerService.model.enums.CustomerType;
import com.project.smartinsurance.customerService.model.enums.KycStatus;
import com.project.smartinsurance.customerService.model.enums.OnboardingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "customer_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity {

    @Column(name = "customer_code", unique = true, nullable = false)
    private String customerCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", insertable = false, updatable = false)
    private CustomerType customerType;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "primary_address", columnDefinition = "TEXT")
    private String primaryAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status")
    private KycStatus kycStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "onboarding_status")
    private OnboardingStatus onboardingStatus;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
