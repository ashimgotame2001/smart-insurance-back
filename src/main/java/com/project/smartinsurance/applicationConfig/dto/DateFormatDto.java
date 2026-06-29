package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.util.UUID;

@Data
public class DateFormatDto {
    private UUID id;
    private String format;
    private String description;
    private Status status;
}
