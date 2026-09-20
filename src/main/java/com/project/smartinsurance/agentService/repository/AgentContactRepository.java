package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentContactRepository extends JpaRepository<AgentContact, UUID> {

    Optional<AgentContact> findByAgentId(UUID agentId);

    boolean existsByPrimaryMobile(String primaryMobile);

    boolean existsByEmail(String email);
}
