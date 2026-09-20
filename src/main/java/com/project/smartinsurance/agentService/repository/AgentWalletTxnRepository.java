package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentWalletTxn;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentWalletTxnRepository extends JpaRepository<AgentWalletTxn, UUID> {

    List<AgentWalletTxn> findByWalletIdAndStatusNotOrderByCreatedAtDesc(UUID walletId, Status status);

    Page<AgentWalletTxn> findByWalletIdAndStatusNot(UUID walletId, Status status, Pageable pageable);
}
