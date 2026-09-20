package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.agentService.model.enums.CommissionStructureType;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "commission_setups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommissionSetup extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    private String agentCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommissionStructureType structureType;

    @Column(precision = 10, scale = 4)
    private BigDecimal defaultRate;

    @Column(precision = 14, scale = 2)
    private BigDecimal minAmount;

    @Column(precision = 14, scale = 2)
    private BigDecimal maxAmount;

    @Builder.Default
    private Boolean renewalEligible = false;

    @Builder.Default
    private Boolean bonusEligible = false;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;
}
