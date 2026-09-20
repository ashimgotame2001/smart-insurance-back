package com.project.smartinsurance.agentService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentTrainingDto {
    private UUID id;
    private String trainingName;
    private String trainingProvider;
    private LocalDate startDate;
    private LocalDate endDate;
    private String certificateNumber;
    private String grade;
    private String remarks;
}
