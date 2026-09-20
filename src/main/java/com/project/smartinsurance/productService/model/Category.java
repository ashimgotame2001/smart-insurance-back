package com.project.smartinsurance.productService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_categories", indexes = {
        @Index(name = "idx_product_category_code", columnList = "category_code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {

    @Column(name = "category_code", nullable = false, unique = true)
    private String code;

    @Column(name = "category_name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "parent_code")
    private String parentCode;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Builder.Default
    private Boolean deleted = false;

    private LocalDateTime deletedAt;
    private String deletedBy;
}