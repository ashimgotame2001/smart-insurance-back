package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class AccountingPeriodDto {
    private UUID id;
    private String name;
    private String code;
    private LocalDate startDate;
    private LocalDate endDate;
    private String fiscalYear;
    private Boolean isClosed;
    private Status status;
}
