package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimInvestigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimInvestigationRepository extends JpaRepository<ClaimInvestigation, UUID> {

    List<ClaimInvestigation> findByClaimIdOrderByCreatedAtDesc(UUID claimId);
    Optional<ClaimInvestigation> findFirstByClaimIdOrderByCreatedAtDesc(UUID claimId);
}
