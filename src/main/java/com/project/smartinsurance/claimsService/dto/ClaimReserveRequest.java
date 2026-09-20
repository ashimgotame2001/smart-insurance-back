package com.project.smartinsurance.claimsService.dto;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimReserveRequest {
    private BigDecimal amount;
    private String reason;
    private String revisedBy;
}
