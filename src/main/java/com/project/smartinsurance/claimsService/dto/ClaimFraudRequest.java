package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.FraudDisposition;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimFraudRequest {
    private Integer score;
    private String indicators;
    private FraudDisposition disposition;
    private String notes;
    private String reviewedBy;
}
