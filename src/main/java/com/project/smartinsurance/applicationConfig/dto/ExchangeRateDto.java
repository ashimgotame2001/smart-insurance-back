package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ExchangeRateDto {
    private UUID id;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private Status status;
}
