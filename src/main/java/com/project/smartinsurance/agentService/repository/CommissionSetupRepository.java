package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.CommissionSetup;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CommissionSetupRepository extends JpaRepository<CommissionSetup, UUID>, JpaSpecificationExecutor<CommissionSetup> {

    boolean existsByCodeIgnoreCaseAndStatusNot(String code, Status status);

    boolean existsByCodeIgnoreCaseAndStatusNotAndIdNot(String code, Status status, UUID id);

    Optional<CommissionSetup> findByIdAndStatusNot(UUID id, Status status);
}
