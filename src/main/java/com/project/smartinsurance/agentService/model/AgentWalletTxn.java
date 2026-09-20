package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.agentService.model.enums.WalletTxnType;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "agent_wallet_txns", indexes = {
        @Index(name = "idx_wallet_txn_wallet", columnList = "wallet_id"),
        @Index(name = "idx_wallet_txn_settlement", columnList = "settlement_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentWalletTxn extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private AgentWallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(name = "txn_type", nullable = false)
    private WalletTxnType txnType;

    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(name = "reference")
    private String reference;

    @Column(name = "settlement_id")
    private UUID settlementId;

    @Column(name = "remarks", length = 1000)
    private String remarks;
}
