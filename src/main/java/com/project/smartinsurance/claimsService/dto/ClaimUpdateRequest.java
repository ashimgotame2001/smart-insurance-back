package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.ClaimPriority;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimUpdateRequest {
    private String lossLocation;
    private String causeOfLoss;
    private String description;
    private BigDecimal estimatedLoss;
    private LocalDate lossDate;
    private LocalTime lossTime;
    private String reportedBy;
    private UUID branchId;
    private ClaimPriority priority;
    private String catastropheCode;
    private String policeFirNumber;
    private LocalDate hospitalAdmissionDate;
    private String customerName;
    private String insuredPartyName;
}
