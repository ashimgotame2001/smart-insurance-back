package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentDocument;
import com.project.smartinsurance.agentService.model.enums.AgentDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentDocumentRepository extends JpaRepository<AgentDocument, UUID> {

    List<AgentDocument> findByAgentId(UUID agentId);

    List<AgentDocument> findByAgentIdAndDocumentType(UUID agentId, AgentDocumentType documentType);
}
