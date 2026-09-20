package com.project.smartinsurance.billingService.dto;

import com.project.smartinsurance.billingService.model.OnlinePayment.OnlinePaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OnlinePaymentDto {
    private UUID id;
    private String transactionId;
    private String gatewayName;
    private String payerName;
    private String payerEmail;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime paymentDate;
    private OnlinePaymentStatus paymentStatus;
    private String gatewayResponse;
    private String invoiceNo;
    private UUID policyId;
    private UUID installmentId;
}
