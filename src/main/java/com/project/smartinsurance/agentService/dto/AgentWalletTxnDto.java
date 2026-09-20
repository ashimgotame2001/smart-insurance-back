package com.project.smartinsurance.agentService.dto;

import com.project.smartinsurance.agentService.model.enums.WalletTxnType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentWalletTxnDto {
    private UUID id;
    private UUID walletId;
    private WalletTxnType txnType;
    private BigDecimal amount;
    private String reference;
    private UUID settlementId;
    private String remarks;
    private LocalDateTime createdAt;
}
