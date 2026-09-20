package com.project.smartinsurance.agentService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentSearchRequest {
    private String agentCode;
    private String name;
    private String mobile;
    private String email;
    private String licenseNumber;
    private String citizenshipNumber;
    private String branchId;
    private String agentType;
    private String agentStatus;
    private String reportingManagerId;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDir = "desc";
}
