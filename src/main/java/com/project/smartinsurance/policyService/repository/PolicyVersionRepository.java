package com.project.smartinsurance.policyService.repository;

import com.project.smartinsurance.policyService.model.PolicyVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PolicyVersionRepository extends JpaRepository<PolicyVersion, UUID> {

    List<PolicyVersion> findByPolicyIdOrderByVersionNumberDesc(UUID policyId);

    Optional<PolicyVersion> findByPolicyIdAndVersionNumber(UUID policyId, int versionNumber);
}
