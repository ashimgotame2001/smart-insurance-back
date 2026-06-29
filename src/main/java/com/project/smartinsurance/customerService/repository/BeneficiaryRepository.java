package com.project.smartinsurance.customerService.repository;

import com.project.smartinsurance.customerService.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, UUID> {
    List<Beneficiary> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);
}
