package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.util.UUID;

@Data
public class BankDto {
    private UUID id;
    private String name;
    private String code;
    private String swiftCode;
    private String bankType;
    private Status status;
}
