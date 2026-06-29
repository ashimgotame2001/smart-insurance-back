package com.project.smartinsurance.customerService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.customerService.model.enums.RiskCategory;
import com.project.smartinsurance.identityService.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "customer_risk_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRiskProfile extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_category", nullable = false)
    private RiskCategory riskCategory;

    @Column(name = "risk_score")
    private Integer riskScore;

    @Column(name = "assessment_date", nullable = false)
    private LocalDate assessmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessed_by")
    private User assessedBy;

    @Column(name = "factors", columnDefinition = "TEXT")
    private String factors;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "next_review_date")
    private LocalDate nextReviewDate;
}
