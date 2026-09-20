package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimFraudCase;
import com.project.smartinsurance.claimsService.model.enums.FraudDisposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClaimFraudCaseRepository extends JpaRepository<ClaimFraudCase, UUID> {

    List<ClaimFraudCase> findByClaimIdOrderByCreatedAtDesc(UUID claimId);

    Optional<ClaimFraudCase> findFirstByClaimIdOrderByCreatedAtDesc(UUID claimId);

    @Query("SELECT COUNT(f) FROM ClaimFraudCase f WHERE f.disposition = :disposition AND f.claim.deleted = false")
    long countByDispositionAndClaimNotDeleted(@Param("disposition") FraudDisposition disposition);
}
