package com.project.smartinsurance.customerService.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRiskProfileDto {
    private UUID id;
    private Status status;
    private UUID customerId;
    private String customerCode;
    private String customerName;
    private String riskCategory;
    private Integer riskScore;
    private LocalDate assessmentDate;
    private UUID assessedById;
    private String assessedByName;
    private String factors;
    private String notes;
    private LocalDate nextReviewDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
