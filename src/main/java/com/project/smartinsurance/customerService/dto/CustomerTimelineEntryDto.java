package com.project.smartinsurance.customerService.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerTimelineEntryDto {
    private UUID id;
    private Status status;
    private UUID customerId;
    private String customerCode;
    private String customerName;
    private String activityType;
    private String description;
    private String referenceType;
    private String referenceId;
    private UUID createdById;
    private String createdByName;
    private LocalDateTime createdAt;
}
