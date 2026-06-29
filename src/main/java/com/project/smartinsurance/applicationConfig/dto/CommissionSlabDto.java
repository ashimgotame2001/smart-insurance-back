package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CommissionSlabDto {
    private UUID id;
    private String name;
    private String code;
    private String agentCategory;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private BigDecimal commissionRate;
    private Status status;
}
