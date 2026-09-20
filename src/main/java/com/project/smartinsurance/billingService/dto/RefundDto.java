package com.project.smartinsurance.billingService.dto;

import com.project.smartinsurance.billingService.model.Refund.RefundStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class RefundDto {
    private UUID id;
    private String refundNo;
    private String invoiceNo;
    private String customerName;
    private BigDecimal amount;
    private LocalDate refundDate;
    private String reason;
    private RefundStatus refundStatus;
}
