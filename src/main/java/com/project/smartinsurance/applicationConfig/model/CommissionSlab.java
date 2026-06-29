package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "master_commission_slabs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommissionSlab extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    private String agentCategory;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal minAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal maxAmount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal commissionRate;
}
