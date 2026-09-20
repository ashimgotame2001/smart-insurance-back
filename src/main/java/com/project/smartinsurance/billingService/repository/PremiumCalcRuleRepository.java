package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.PremiumCalcRule;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PremiumCalcRuleRepository extends JpaRepository<PremiumCalcRule, UUID> {
    List<PremiumCalcRule> findAllByStatus(Status status);
    Optional<PremiumCalcRule> findByRuleCode(String ruleCode);
    Optional<PremiumCalcRule> findFirstByProductCodeAndPlanCodeAndActiveTrueAndStatus(String productCode, String planCode, Status status);
    Optional<PremiumCalcRule> findFirstByProductCodeAndActiveTrueAndStatus(String productCode, Status status);
}
