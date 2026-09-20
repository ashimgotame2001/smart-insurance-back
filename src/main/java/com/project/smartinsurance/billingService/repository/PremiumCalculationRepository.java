package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.PremiumCalculation;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PremiumCalculationRepository extends JpaRepository<PremiumCalculation, UUID> {
    List<PremiumCalculation> findAllByStatusOrderByCalculatedAtDesc(Status status);
    List<PremiumCalculation> findByPolicyIdAndStatus(UUID policyId, Status status);
}
