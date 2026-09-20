package com.project.smartinsurance.policyService.repository;

import com.project.smartinsurance.policyService.model.Endorsement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EndorsementRepository extends JpaRepository<Endorsement, UUID> {

    List<Endorsement> findByPolicyIdOrderByCreatedAtDesc(UUID policyId);

    Optional<Endorsement> findByEndorsementNumber(String endorsementNumber);
}
