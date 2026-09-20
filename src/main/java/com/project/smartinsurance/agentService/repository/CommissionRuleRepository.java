package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.CommissionRule;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CommissionRuleRepository extends JpaRepository<CommissionRule, UUID>, JpaSpecificationExecutor<CommissionRule> {

    boolean existsByCodeIgnoreCaseAndStatusNot(String code, Status status);

    boolean existsByCodeIgnoreCaseAndStatusNotAndIdNot(String code, Status status, UUID id);
}
