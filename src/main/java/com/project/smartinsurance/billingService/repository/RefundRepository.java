package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.Refund;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefundRepository extends JpaRepository<Refund, UUID> {
    List<Refund> findAllByStatus(Status status);
    Optional<Refund> findByRefundNo(String refundNo);
}
