package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.SalvageStatus;
import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimSalvageRequest {
    private String itemDescription;
    private BigDecimal estimatedValue;
    private BigDecimal realizedValue;
    private SalvageStatus salvageStatus;
    private String remarks;
}
