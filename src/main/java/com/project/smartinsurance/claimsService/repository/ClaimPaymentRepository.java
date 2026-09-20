package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimPaymentRepository extends JpaRepository<ClaimPayment, UUID> {

    List<ClaimPayment> findByClaimIdOrderByCreatedAtDesc(UUID claimId);
}
