package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimDecisionRepository extends JpaRepository<ClaimDecision, UUID> {

    List<ClaimDecision> findByClaimIdOrderByDecidedAtDesc(UUID claimId);
}
