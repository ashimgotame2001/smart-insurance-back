package com.project.smartinsurance.policyService.repository;

import com.project.smartinsurance.policyService.model.PolicyAuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyAuditTrailRepository extends JpaRepository<PolicyAuditTrail, UUID> {

    List<PolicyAuditTrail> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(String entityType, String entityId);
}
