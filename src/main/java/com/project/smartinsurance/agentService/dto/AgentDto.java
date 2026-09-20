package com.project.smartinsurance.agentService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentDto {
    private UUID id;
    private String agentCode;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String nationality;
    private String citizenshipNumber;
    private String maritalStatus;
    private String fatherName;
    private String motherName;
    private String spouseName;
    private String agentType;
    private String agentStatus;
    private String employmentStatus;
    private String workingStatus;
    private LocalDate joiningDate;
    private UUID branchId;
    private UUID regionId;
    private UUID reportingManagerId;
    private UUID teamLeaderId;
    private String salesOffice;
    private String salesChannel;
    private String territory;
    private UUID profilePhotoDocId;
    private String approvalStatus;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AgentContactDto contact;
    private List<AgentAddressDto> addresses;
    private List<AgentLicenseDto> licenses;
    private List<AgentBankDto> bankAccounts;
    private AgentPerformanceDto performance;
    private AgentHierarchyDto hierarchy;
}
