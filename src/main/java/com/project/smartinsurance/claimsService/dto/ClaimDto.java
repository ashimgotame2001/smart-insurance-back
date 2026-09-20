package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.ClaimPriority;
import com.project.smartinsurance.claimsService.model.enums.ClaimStatus;
import com.project.smartinsurance.claimsService.model.enums.ClaimType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimDto {
    private UUID id;
    private String claimNumber;
    private UUID policyId;
    private String policyNumber;
    private UUID customerId;
    private String customerName;
    private UUID insuredPartyId;
    private String insuredPartyName;
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
    private ClaimStatus claimStatus;
    private BigDecimal reserveAmount;
    private BigDecimal approvedAmount;
    private BigDecimal settledAmount;
    private BigDecimal paidAmount;
    private String currency;
    private ClaimPriority priority;
    private Boolean fraudFlag;
    private String catastropheCode;
    private String policeFirNumber;
    private LocalDate hospitalAdmissionDate;
    private String verificationNotes;
    private Boolean eligibilityPassed;
    private String onHoldReason;
    private Boolean litigationFlag;
    private BigDecimal reinsuranceShareAmount;
    private String reinsuranceShareStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ClaimCoverageItemDto> coverages;
    private List<ClaimPartyDto> parties;
    private List<ClaimDocumentDto> documents;
    private ClaimInvestigationDto latestInvestigation;
    private ClaimFraudDto latestFraud;
    private ClaimSurveyDto latestSurvey;
    private ClaimMedicalReviewDto latestMedicalReview;
    private List<ClaimSettlementDto> settlements;
    private List<ClaimPaymentDto> payments;
    private List<ClaimRecoveryDto> recoveries;
    private List<ClaimSalvageDto> salvages;
    private List<ClaimEventDto> recentEvents;
}
