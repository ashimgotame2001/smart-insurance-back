package com.project.smartinsurance.agentService.dto;

import com.project.smartinsurance.agentService.model.enums.CommissionStructureType;
import com.project.smartinsurance.commonService.dto.PageFilterRequest;
import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommissionSetupFilterRequest extends PageFilterRequest {

    private String code;
    private String name;
    private String agentCategory;
    private CommissionStructureType structureType;
    private Status status;
}
