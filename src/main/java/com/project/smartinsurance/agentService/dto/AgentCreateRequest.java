package com.project.smartinsurance.agentService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentCreateRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String middleName;
    private String gender;
    private LocalDate dateOfBirth;
    private String nationality;

    @NotBlank
    private String citizenshipNumber;

    private String maritalStatus;
    private String fatherName;
    private String motherName;
    private String spouseName;

    @NotNull
    private String agentType;

    private String employmentStatus;
    private String workingStatus;
    private LocalDate joiningDate;
    private UUID branchId;
    private UUID regionId;
    private String salesOffice;
    private String salesChannel;
    private String territory;
    private UUID reportingManagerId;
    private UUID teamLeaderId;
    private AgentContactDto contact;
    private List<AgentAddressDto> addresses;
}
