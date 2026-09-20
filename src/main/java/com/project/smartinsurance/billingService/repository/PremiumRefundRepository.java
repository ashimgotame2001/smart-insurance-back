package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.PremiumRefund;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PremiumRefundRepository extends JpaRepository<PremiumRefund, UUID> {
    List<PremiumRefund> findAllByStatus(Status status);
    Optional<PremiumRefund> findByRefundNo(String refundNo);
}
