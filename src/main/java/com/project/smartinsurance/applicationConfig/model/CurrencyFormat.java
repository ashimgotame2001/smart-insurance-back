package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "master_currency_formats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyFormat extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String locale;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;
}
