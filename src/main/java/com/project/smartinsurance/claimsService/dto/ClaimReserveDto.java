package com.project.smartinsurance.claimsService.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimReserveDto {
    private UUID id;
    private BigDecimal amount;
    private BigDecimal previousAmount;
    private String reason;
    private String revisedBy;
    private LocalDateTime revisedAt;
}
