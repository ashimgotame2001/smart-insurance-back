package com.project.smartinsurance.customerService.repository;

import com.project.smartinsurance.customerService.model.KYC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KYCRepository extends JpaRepository<KYC, UUID> {
    List<KYC> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);
    List<KYC> findByVerificationStatus(String verificationStatus);
}
