package com.project.smartinsurance.agentService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentHierarchyDto {
    private UUID id;
    private UUID parentAgentId;
    private UUID agencyManagerId;
    private UUID seniorAgentId;
    private String teamName;
    private UUID branchId;
    private Integer hierarchyLevel;
}
