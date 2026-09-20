package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentHierarchyRepository extends JpaRepository<AgentHierarchy, UUID> {

    Optional<AgentHierarchy> findByAgentId(UUID agentId);

    List<AgentHierarchy> findByParentAgentId(UUID parentAgentId);
}
