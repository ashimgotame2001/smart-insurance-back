package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.util.UUID;

@Data
public class DistrictDto {
    private UUID id;
    private String code;
    private String name;
    private UUID countryId;
    private RefDto country;
    private Status status;
}
