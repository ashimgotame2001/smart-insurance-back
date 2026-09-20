package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.RecoveryStatus;
import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimRecoveryRequest {
    private String source;
    private BigDecimal amount;
    private RecoveryStatus recoveryStatus;
    private String remarks;
}
