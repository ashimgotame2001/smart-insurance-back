package com.project.smartinsurance.agentService.dto;

import com.project.smartinsurance.agentService.model.enums.WalletTxnType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletAdjustRequest {

    @NotNull
    private WalletTxnType type;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal amount;

    private String reference;

    private String remarks;
}
