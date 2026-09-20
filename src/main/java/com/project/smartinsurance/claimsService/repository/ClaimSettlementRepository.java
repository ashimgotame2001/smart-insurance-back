package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimSettlementRepository extends JpaRepository<ClaimSettlement, UUID> {

    List<ClaimSettlement> findByClaimIdOrderBySettledAtDesc(UUID claimId);
}
