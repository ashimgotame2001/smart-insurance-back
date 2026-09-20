package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimPartyRepository extends JpaRepository<ClaimParty, UUID> {

    List<ClaimParty> findByClaimId(UUID claimId);
}
