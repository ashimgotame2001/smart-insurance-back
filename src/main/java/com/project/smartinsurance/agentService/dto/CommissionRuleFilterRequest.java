package com.project.smartinsurance.agentService.dto;

import com.project.smartinsurance.agentService.model.enums.CommissionRuleType;
import com.project.smartinsurance.commonService.dto.PageFilterRequest;
import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommissionRuleFilterRequest extends PageFilterRequest {

    private String code;
    private String name;
    private UUID commissionSetupId;
    private UUID productId;
    private CommissionRuleType ruleType;
    private String agentCategory;
    private Status status;
}
