package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "master_lookup_values", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"category", "code"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LookupValue extends BaseEntity {

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String value;

    private String description;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
