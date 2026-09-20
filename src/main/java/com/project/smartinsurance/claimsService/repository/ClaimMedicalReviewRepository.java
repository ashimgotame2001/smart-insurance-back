package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimMedicalReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimMedicalReviewRepository extends JpaRepository<ClaimMedicalReview, UUID> {

    List<ClaimMedicalReview> findByClaimIdOrderByCreatedAtDesc(UUID claimId);
    Optional<ClaimMedicalReview> findFirstByClaimIdOrderByCreatedAtDesc(UUID claimId);
}
