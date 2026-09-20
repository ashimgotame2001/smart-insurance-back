package com.project.smartinsurance.productService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.productService.model.enums.ExclusionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "exclusions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exclusion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ExclusionType exclusionType;

    private String coverageCode;

    @Column(columnDefinition = "TEXT")
    private String reason;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    @Builder.Default
    private Boolean deleted = false;

    private LocalDateTime deletedAt;
    private String deletedBy;
}