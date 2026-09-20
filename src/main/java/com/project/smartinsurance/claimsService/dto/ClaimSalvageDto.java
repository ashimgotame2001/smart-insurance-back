package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.SalvageStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimSalvageDto {
    private UUID id;
    private String itemDescription;
    private BigDecimal estimatedValue;
    private BigDecimal realizedValue;
    private SalvageStatus salvageStatus;
    private LocalDateTime disposedAt;
    private String remarks;
}
