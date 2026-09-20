package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.OnlinePayment;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OnlinePaymentRepository extends JpaRepository<OnlinePayment, UUID> {
    List<OnlinePayment> findAllByStatus(Status status);
    Optional<OnlinePayment> findByTransactionId(String transactionId);
}
