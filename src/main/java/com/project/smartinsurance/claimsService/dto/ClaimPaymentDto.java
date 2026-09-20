package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.PaymentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimPaymentDto {
    private UUID id;
    private BigDecimal amount;
    private String payeeName;
    private String paymentRef;
    private String utr;
    private PaymentStatus paymentStatus;
    private LocalDateTime paidAt;
    private UUID billingPaymentId;
    private String remarks;
}
