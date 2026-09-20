package com.project.smartinsurance.productService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_versions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVersion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer versionNumber;

    @Column(nullable = false)
    private String changeDescription;

    @Column(nullable = false)
    private String changedBy;

    @Column(columnDefinition = "TEXT")
    private String snapshotJson;

    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
}
