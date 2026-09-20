package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.RecoveryStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimRecoveryDto {
    private UUID id;
    private String source;
    private BigDecimal amount;
    private RecoveryStatus recoveryStatus;
    private LocalDateTime collectedAt;
    private String remarks;
}
