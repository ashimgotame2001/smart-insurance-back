package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.BillingPayment;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BillingPaymentRepository extends JpaRepository<BillingPayment, UUID> {
    List<BillingPayment> findAllByStatus(Status status);
    Optional<BillingPayment> findByPaymentRef(String paymentRef);
    List<BillingPayment> findByPolicyIdAndStatus(UUID policyId, Status status);
    List<BillingPayment> findByPaymentDateBetweenAndStatus(LocalDate from, LocalDate to, Status status);
}
