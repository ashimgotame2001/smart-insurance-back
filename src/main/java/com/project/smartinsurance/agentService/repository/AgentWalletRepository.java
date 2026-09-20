package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentWallet;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentWalletRepository extends JpaRepository<AgentWallet, UUID> {

    Optional<AgentWallet> findByAgentIdAndStatusNot(UUID agentId, Status status);

    Optional<AgentWallet> findByAgentId(UUID agentId);
}
