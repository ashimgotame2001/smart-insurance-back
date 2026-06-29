package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.applicationConfig.model.enums.BranchStatus;
import com.project.smartinsurance.applicationConfig.model.enums.BranchType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDto {

    private UUID id;
    private Status status;
    private String branchCode;
    private String branchName;
    private BranchType branchType;
    private BranchStatus branchStatus;
    private UUID parentBranchId;
    private String email;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private boolean isHeadOffice;
}
