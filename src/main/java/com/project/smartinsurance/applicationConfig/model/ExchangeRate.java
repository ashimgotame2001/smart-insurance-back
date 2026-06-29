package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "master_exchange_rates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRate extends BaseEntity {

    @Column(nullable = false, length = 3)
    private String fromCurrency;

    @Column(nullable = false, length = 3)
    private String toCurrency;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal rate;

    @Column(nullable = false)
    private LocalDate effectiveDate;

    private LocalDate expiryDate;
}
