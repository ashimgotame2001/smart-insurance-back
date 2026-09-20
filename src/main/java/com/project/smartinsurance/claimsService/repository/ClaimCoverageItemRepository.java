package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimCoverageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimCoverageItemRepository extends JpaRepository<ClaimCoverageItem, UUID> {

    List<ClaimCoverageItem> findByClaimId(UUID claimId);
}
