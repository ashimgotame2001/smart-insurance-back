package com.project.smartinsurance.underwritingService.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UwAssignRequest {
    private String assignee;
    private String priority;
    private String notes;
}
