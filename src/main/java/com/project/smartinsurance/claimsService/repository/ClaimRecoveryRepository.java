package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimRecovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimRecoveryRepository extends JpaRepository<ClaimRecovery, UUID> {

    List<ClaimRecovery> findByClaimIdOrderByCreatedAtDesc(UUID claimId);
}
