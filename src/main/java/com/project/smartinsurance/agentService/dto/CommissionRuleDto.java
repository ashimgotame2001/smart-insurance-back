package com.project.smartinsurance.agentService.dto;

import com.project.smartinsurance.agentService.model.enums.CommissionCalculationBasis;
import com.project.smartinsurance.agentService.model.enums.CommissionRuleType;
import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CommissionRuleDto {
    private UUID id;
    private String code;
    private String name;
    private UUID commissionSetupId;
    private String commissionSetupCode;
    private String commissionSetupName;
    private UUID productId;
    private String agentCategory;
    private CommissionRuleType ruleType;
    private CommissionCalculationBasis calculationBasis;
    private BigDecimal rate;
    private BigDecimal fixedAmount;
    private Integer priority;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Status entityStatus;
}
