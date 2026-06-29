package com.project.smartinsurance.customerService.repository;

import com.project.smartinsurance.customerService.model.CustomerRiskProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRiskProfileRepository extends JpaRepository<CustomerRiskProfile, UUID> {
    List<CustomerRiskProfile> findByCustomerIdOrderByAssessmentDateDesc(UUID customerId);
    Optional<CustomerRiskProfile> findTopByCustomerIdOrderByAssessmentDateDesc(UUID customerId);
}
