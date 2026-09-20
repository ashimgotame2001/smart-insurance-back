package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentCommission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentCommissionRepository extends JpaRepository<AgentCommission, UUID> {

    List<AgentCommission> findByAgentId(UUID agentId);

    Optional<AgentCommission> findByAgentIdAndEffectiveToIsNull(UUID agentId);
}
