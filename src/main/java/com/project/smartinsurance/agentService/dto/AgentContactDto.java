package com.project.smartinsurance.agentService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentContactDto {
    private UUID id;

    @NotBlank
    private String primaryMobile;

    private String secondaryMobile;

    @Email
    private String email;

    private String emergencyContactName;
    private String emergencyContactNumber;
}
