package com.project.smartinsurance.policyService.dto;

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
public class PolicyVersionDto {
    private UUID id;
    private UUID policyId;
    private int versionNumber;
    private String changeType;
    private String sourceReference;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private String snapshotData;
    private String createdBy;
    private LocalDateTime createdAt;
}
