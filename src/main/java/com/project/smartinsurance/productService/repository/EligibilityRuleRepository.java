package com.project.smartinsurance.productService.repository;

import com.project.smartinsurance.productService.model.EligibilityRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EligibilityRuleRepository extends JpaRepository<EligibilityRule, UUID> {
    List<EligibilityRule> findByProductId(UUID productId);
}
