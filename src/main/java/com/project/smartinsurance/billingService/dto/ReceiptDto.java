package com.project.smartinsurance.billingService.dto;

import com.project.smartinsurance.billingService.model.Receipt.ReceiptStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ReceiptDto {
    private UUID id;
    private String receiptNo;
    private String customerName;
    private String customerEmail;
    private BigDecimal amount;
    private LocalDate receiptDate;
    private String paymentMode;
    private String referenceNo;
    private String description;
    private ReceiptStatus receiptStatus;
}
