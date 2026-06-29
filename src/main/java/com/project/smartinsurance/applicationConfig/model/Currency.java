package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "master_currencies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true, length = 3)
    private String code;

    @Column(length = 5)
    private String symbol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
}
