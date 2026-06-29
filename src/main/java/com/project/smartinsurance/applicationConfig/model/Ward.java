package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "master_wards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ward extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "ward_number")
    private Integer wardNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;
}
