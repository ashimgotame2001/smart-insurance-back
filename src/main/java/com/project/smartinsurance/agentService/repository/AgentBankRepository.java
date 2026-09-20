package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentBankRepository extends JpaRepository<AgentBank, UUID> {

    List<AgentBank> findByAgentId(UUID agentId);

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByPanNumber(String panNumber);
}
