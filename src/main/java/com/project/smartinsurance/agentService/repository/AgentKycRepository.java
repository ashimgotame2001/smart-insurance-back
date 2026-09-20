package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentKyc;
import com.project.smartinsurance.agentService.model.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentKycRepository extends JpaRepository<AgentKyc, UUID> {

    List<AgentKyc> findByAgentId(UUID agentId);

    List<AgentKyc> findByAgentIdAndVerificationStatus(UUID agentId, VerificationStatus verificationStatus);
}
