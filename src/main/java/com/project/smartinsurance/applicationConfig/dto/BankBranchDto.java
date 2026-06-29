package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.util.UUID;

@Data
public class BankBranchDto {
    private UUID id;
    private String name;
    private String code;
    private String address;
    private String city;
    private String phone;
    private String swiftCode;
    private UUID bankId;
    private RefDto bank;
    private Status status;
}
