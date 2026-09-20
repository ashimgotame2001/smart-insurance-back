package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentSettlement;
import com.project.smartinsurance.agentService.model.enums.SettlementStatus;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentSettlementRepository extends JpaRepository<AgentSettlement, UUID> {

    Optional<AgentSettlement> findByIdAndStatusNot(UUID id, Status status);

    Page<AgentSettlement> findByStatusNot(Status status, Pageable pageable);

    List<AgentSettlement> findByStatusNot(Status status);

    List<AgentSettlement> findByAgentIdAndStatusNot(UUID agentId, Status status);

    Page<AgentSettlement> findByAgentIdAndStatusNot(UUID agentId, Status status, Pageable pageable);

    List<AgentSettlement> findBySettlementStatusAndStatusNot(SettlementStatus settlementStatus, Status status);
}
