package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "master_reinsurance_companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReinsuranceCompany extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    private String country;
    private String rating;
    private String contactEmail;
    private String contactPhone;
}
