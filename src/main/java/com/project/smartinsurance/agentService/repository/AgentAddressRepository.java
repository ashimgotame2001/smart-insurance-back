package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentAddress;
import com.project.smartinsurance.agentService.model.enums.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentAddressRepository extends JpaRepository<AgentAddress, UUID> {

    List<AgentAddress> findByAgentId(UUID agentId);

    Optional<AgentAddress> findByAgentIdAndAddressType(UUID agentId, AddressType addressType);
}
