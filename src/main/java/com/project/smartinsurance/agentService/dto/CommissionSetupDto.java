package com.project.smartinsurance.agentService.dto;

import com.project.smartinsurance.agentService.model.enums.CommissionStructureType;
import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CommissionSetupDto {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private String agentCategory;
    private CommissionStructureType structureType;
    private BigDecimal defaultRate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Boolean renewalEligible;
    private Boolean bonusEligible;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Status entityStatus;
}
