package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentLicense;
import com.project.smartinsurance.agentService.model.enums.LicenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentLicenseRepository extends JpaRepository<AgentLicense, UUID> {

    List<AgentLicense> findByAgentId(UUID agentId);

    Optional<AgentLicense> findByLicenseNumber(String licenseNumber);

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByAgentIdAndLicenseStatus(UUID agentId, LicenseStatus licenseStatus);
}
