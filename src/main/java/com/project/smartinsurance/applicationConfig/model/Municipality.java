package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "master_municipalities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Municipality extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    private String municipalityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;
}
