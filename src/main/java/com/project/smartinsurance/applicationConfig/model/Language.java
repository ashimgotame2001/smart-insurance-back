package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "master_languages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Language extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;
}
