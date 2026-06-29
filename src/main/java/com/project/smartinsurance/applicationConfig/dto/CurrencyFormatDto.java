package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.util.UUID;

@Data
public class CurrencyFormatDto {
    private UUID id;
    private String locale;
    private String name;
    private String description;
    private Status status;
}
