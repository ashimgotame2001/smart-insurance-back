package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentTrainingRepository extends JpaRepository<AgentTraining, UUID> {

    List<AgentTraining> findByAgentId(UUID agentId);
}
