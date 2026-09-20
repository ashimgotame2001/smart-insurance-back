package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.ClaimDecisionType;
import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimDecisionRequest {
    private ClaimDecisionType decisionType;
    private BigDecimal amount;
    private String remarks;
    private String authorityLevel;
    private String decidedBy;
    private String checkerBy;
}
