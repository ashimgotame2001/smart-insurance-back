package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.util.UUID;

@Data
public class CurrencyDto {
    private UUID id;
    private String name;
    private String code;
    private String symbol;
    private UUID countryId;
    private RefDto country;
    private Status status;
}
