package com.project.smartinsurance.billingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumDayEndDto {
    private LocalDate businessDate;
    private UUID branchId;
    private long collectionsCount;
    private BigDecimal collectionsAmount;
    private long receiptsCount;
    private BigDecimal outstandingAmount;
    private String message;
}
