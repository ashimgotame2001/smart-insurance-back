package com.project.smartinsurance.billingService.dto;

import com.project.smartinsurance.billingService.model.Invoice.InvoiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class InvoiceDto {
    private UUID id;
    private String invoiceNo;
    private UUID policyId;
    private String policyNumber;
    private UUID customerId;
    private UUID branchId;
    private UUID installmentId;
    private String customerName;
    private String customerEmail;
    private BigDecimal amount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private String description;
    private InvoiceStatus invoiceStatus;
}
