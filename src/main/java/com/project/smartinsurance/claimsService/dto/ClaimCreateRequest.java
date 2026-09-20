package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.ClaimPriority;
import com.project.smartinsurance.claimsService.model.enums.ClaimType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimCreateRequest {
    private UUID policyId;
    private String policyNumber;
    private UUID customerId;
    private String customerName;
    private UUID insuredPartyId;
    private String insuredPartyName;
    @NotNull
    private ClaimType claimType;
    private LocalDate lossDate;
    private LocalTime lossTime;
    private String lossLocation;
    private String causeOfLoss;
    private String description;
    private BigDecimal estimatedLoss;
    private LocalDateTime intimatedAt;
    private String reportedBy;
    private UUID branchId;
    private ClaimPriority priority;
    private String catastropheCode;
    private String policeFirNumber;
    private LocalDate hospitalAdmissionDate;
    private List<ClaimCoverageItemDto> coverages;
    private List<ClaimPartyDto> parties;
}
