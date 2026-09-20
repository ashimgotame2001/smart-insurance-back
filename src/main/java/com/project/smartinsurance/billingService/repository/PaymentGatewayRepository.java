package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.PaymentGateway;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentGatewayRepository extends JpaRepository<PaymentGateway, UUID> {
    List<PaymentGateway> findAllByStatus(Status status);
    List<PaymentGateway> findByActiveTrueAndStatus(Status status);
}
