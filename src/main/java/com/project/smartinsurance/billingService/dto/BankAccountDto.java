package com.project.smartinsurance.billingService.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;

import java.util.UUID;

@Data
public class BankAccountDto {
    private UUID id;
    private String accountName;
    private String accountNumber;
    private String bankName;
    private String branchName;
    private String swiftCode;
    private String currency;
    private String accountHolder;
    private String notes;
    private Status status;
}
