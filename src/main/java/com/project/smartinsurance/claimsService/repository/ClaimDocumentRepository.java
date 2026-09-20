package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimDocumentRepository extends JpaRepository<ClaimDocument, UUID> {

    List<ClaimDocument> findByClaimId(UUID claimId);
    Optional<ClaimDocument> findByIdAndClaimId(UUID id, UUID claimId);
}
