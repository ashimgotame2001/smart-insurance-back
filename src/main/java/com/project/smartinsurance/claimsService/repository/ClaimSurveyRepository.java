package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimSurveyRepository extends JpaRepository<ClaimSurvey, UUID> {

    List<ClaimSurvey> findByClaimIdOrderByCreatedAtDesc(UUID claimId);
    Optional<ClaimSurvey> findFirstByClaimIdOrderByCreatedAtDesc(UUID claimId);
}
