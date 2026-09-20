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
public class AgentBankDto {
    private UUID id;
    private UUID bankId;
    private String branchName;
    private String accountHolderName;
    private String accountNumber;
    private String swiftCode;
    private String panNumber;
    private String taxRegistrationNumber;
    private Boolean isPrimary;
}
