package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.PremiumAdjustment;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PremiumAdjustmentRepository extends JpaRepository<PremiumAdjustment, UUID> {
    List<PremiumAdjustment> findAllByStatus(Status status);
    Optional<PremiumAdjustment> findByAdjustmentNo(String adjustmentNo);
}
