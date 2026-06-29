package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.util.UUID;

@Data
public class LookupValueDto {
    private UUID id;
    private String category;
    private String code;
    private String value;
    private String description;
    private Integer sortOrder;
    private Status status;
}
