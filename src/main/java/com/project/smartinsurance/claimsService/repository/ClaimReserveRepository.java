package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimReserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimReserveRepository extends JpaRepository<ClaimReserve, UUID> {

    List<ClaimReserve> findByClaimIdOrderByRevisedAtDesc(UUID claimId);
}
