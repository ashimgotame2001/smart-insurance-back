package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.util.UUID;

@Data
public class MunicipalityDto {
    private UUID id;
    private String name;
    private String code;
    private String municipalityType;
    private UUID districtId;
    private RefDto district;
    private Status status;
}
