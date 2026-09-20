package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.FraudDisposition;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimFraudDto {
    private UUID id;
    private Integer score;
    private String indicators;
    private FraudDisposition disposition;
    private String notes;
    private String reviewedBy;
    private LocalDateTime reviewedAt;
}
