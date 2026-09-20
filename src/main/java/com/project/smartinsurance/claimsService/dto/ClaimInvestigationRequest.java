package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.InvestigationOutcome;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimInvestigationRequest {
    private String assignedTo;
    private LocalDate dueDate;
    private String notes;
    private InvestigationOutcome outcome;
}
