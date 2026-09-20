package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.PaymentAllocation;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentAllocationRepository extends JpaRepository<PaymentAllocation, UUID> {
    List<PaymentAllocation> findByPaymentIdAndStatus(UUID paymentId, Status status);
    List<PaymentAllocation> findByInstallmentIdAndStatus(UUID installmentId, Status status);
}
