package com.project.smartinsurance.policyService.repository;

import com.project.smartinsurance.policyService.model.Coverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoverageRepository extends JpaRepository<Coverage, UUID> {

    List<Coverage> findByPolicyId(UUID policyId);

    Optional<Coverage> findByPolicyIdAndCoverageCode(UUID policyId, String coverageCode);
}
