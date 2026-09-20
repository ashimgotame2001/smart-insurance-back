package com.project.smartinsurance.agentService.service;

import com.project.smartinsurance.agentService.dto.AgentWalletDto;
import com.project.smartinsurance.agentService.dto.AgentWalletTxnDto;
import com.project.smartinsurance.agentService.dto.WalletAdjustRequest;
import com.project.smartinsurance.agentService.model.AgentWallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AgentWalletService {

    AgentWallet getOrCreate(UUID agentId);

    AgentWalletDto getWallet(UUID agentId);

    List<AgentWalletTxnDto> listTransactions(UUID agentId);

    AgentWalletDto adjust(UUID agentId, WalletAdjustRequest request);

    /**
     * Credits the agent's wallet for a paid settlement and records a CREDIT txn.
     */
    void creditFromSettlement(UUID agentId, BigDecimal amount, UUID settlementId, String remarks);
}
