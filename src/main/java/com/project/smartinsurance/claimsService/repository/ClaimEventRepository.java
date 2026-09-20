package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimEventRepository extends JpaRepository<ClaimEvent, UUID> {

    List<ClaimEvent> findByClaimIdOrderByOccurredAtDesc(UUID claimId);
    List<ClaimEvent> findTop20ByClaimIdOrderByOccurredAtDesc(UUID claimId);
}
