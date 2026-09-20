package com.project.smartinsurance.agentService.service.impl;

import com.project.smartinsurance.agentService.dto.AgentWalletDto;
import com.project.smartinsurance.agentService.dto.AgentWalletTxnDto;
import com.project.smartinsurance.agentService.dto.WalletAdjustRequest;
import com.project.smartinsurance.agentService.model.Agent;
import com.project.smartinsurance.agentService.model.AgentWallet;
import com.project.smartinsurance.agentService.model.AgentWalletTxn;
import com.project.smartinsurance.agentService.model.enums.WalletTxnType;
import com.project.smartinsurance.agentService.repository.AgentRepository;
import com.project.smartinsurance.agentService.repository.AgentWalletRepository;
import com.project.smartinsurance.agentService.repository.AgentWalletTxnRepository;
import com.project.smartinsurance.agentService.service.AgentWalletService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgentWalletServiceImpl implements AgentWalletService {

    private final AgentWalletRepository walletRepository;
    private final AgentWalletTxnRepository txnRepository;
    private final AgentRepository agentRepository;

    @Override
    @Transactional
    public AgentWallet getOrCreate(UUID agentId) {
        Agent agent = findAgentOrThrow(agentId);
        return walletRepository.findByAgentId(agentId)
                .orElseGet(() -> {
                    AgentWallet wallet = AgentWallet.builder()
                            .agent(agent)
                            .balance(BigDecimal.ZERO)
                            .currency("NPR")
                            .build();
                    wallet.setStatus(Status.ACTIVE);
                    return walletRepository.save(wallet);
                });
    }

    @Override
    @Transactional
    public AgentWalletDto getWallet(UUID agentId) {
        return toDto(getOrCreate(agentId));
    }

    @Override
    @Transactional
    public List<AgentWalletTxnDto> listTransactions(UUID agentId) {
        AgentWallet wallet = getOrCreate(agentId);
        return txnRepository.findByWalletIdAndStatusNotOrderByCreatedAtDesc(wallet.getId(), Status.DELETED)
                .stream()
                .map(this::toTxnDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AgentWalletDto adjust(UUID agentId, WalletAdjustRequest request) {
        if (request.getType() == null) {
            throw new GlobalException("AGT-032", "txn type is required");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new GlobalException("AGT-032", "amount must be greater than zero");
        }

        AgentWallet wallet = getOrCreate(agentId);
        applyTxn(wallet, request.getType(), request.getAmount(), request.getReference(), null, request.getRemarks());
        return toDto(walletRepository.save(wallet));
    }

    @Override
    @Transactional
    public void creditFromSettlement(UUID agentId, BigDecimal amount, UUID settlementId, String remarks) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new GlobalException("AGT-032", "settlement credit amount must be greater than zero");
        }
        AgentWallet wallet = getOrCreate(agentId);
        applyTxn(wallet, WalletTxnType.CREDIT, amount, "SETTLEMENT", settlementId, remarks);
        walletRepository.save(wallet);
    }

    private void applyTxn(AgentWallet wallet, WalletTxnType type, BigDecimal amount,
                          String reference, UUID settlementId, String remarks) {
        BigDecimal balance = wallet.getBalance() != null ? wallet.getBalance() : BigDecimal.ZERO;

        if (type == WalletTxnType.CREDIT) {
            wallet.setBalance(balance.add(amount));
        } else if (type == WalletTxnType.DEBIT) {
            if (balance.compareTo(amount) < 0) {
                throw new GlobalException("AGT-031", balance.toPlainString());
            }
            wallet.setBalance(balance.subtract(amount));
        } else {
            throw new GlobalException("AGT-032", "unsupported txn type: " + type);
        }

        AgentWalletTxn txn = AgentWalletTxn.builder()
                .wallet(wallet)
                .txnType(type)
                .amount(amount)
                .reference(reference)
                .settlementId(settlementId)
                .remarks(remarks)
                .build();
        txn.setStatus(Status.ACTIVE);
        txnRepository.save(txn);
    }

    private Agent findAgentOrThrow(UUID agentId) {
        return agentRepository.findByIdAndDeletedFalse(agentId)
                .orElseThrow(() -> new GlobalException("AGT-001", "Agent not found with id: " + agentId));
    }

    private AgentWalletDto toDto(AgentWallet wallet) {
        Agent agent = wallet.getAgent();
        return AgentWalletDto.builder()
                .id(wallet.getId())
                .agentId(agent != null ? agent.getId() : null)
                .agentCode(agent != null ? agent.getAgentCode() : null)
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .createdAt(wallet.getCreatedAt())
                .updatedAt(wallet.getUpdatedAt())
                .build();
    }

    private AgentWalletTxnDto toTxnDto(AgentWalletTxn txn) {
        return AgentWalletTxnDto.builder()
                .id(txn.getId())
                .walletId(txn.getWallet() != null ? txn.getWallet().getId() : null)
                .txnType(txn.getTxnType())
                .amount(txn.getAmount())
                .reference(txn.getReference())
                .settlementId(txn.getSettlementId())
                .remarks(txn.getRemarks())
                .createdAt(txn.getCreatedAt())
                .build();
    }
}
