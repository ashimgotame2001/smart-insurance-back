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
public class AgentAddressDto {
    private UUID id;
    private String addressType;
    private UUID provinceId;
    private UUID districtId;
    private UUID municipalityId;
    private UUID wardId;
    private String street;
    private String postalCode;
    private Boolean sameAsPermanent;
}
