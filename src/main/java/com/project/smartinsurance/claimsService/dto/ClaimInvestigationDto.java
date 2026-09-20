package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.InvestigationOutcome;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimInvestigationDto {
    private UUID id;
    private String assignedTo;
    private LocalDate dueDate;
    private String notes;
    private InvestigationOutcome outcome;
    private LocalDateTime completedAt;
}
