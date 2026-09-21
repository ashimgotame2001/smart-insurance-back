package com.project.smartinsurance.underwritingService.repository;

import com.project.smartinsurance.underwritingService.model.UwApprovalStep;
import com.project.smartinsurance.underwritingService.model.enums.UwApprovalStepStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UwApprovalStepRepository extends JpaRepository<UwApprovalStep, UUID> {
    List<UwApprovalStep> findByCaseIdOrderByStepOrderAsc(UUID caseId);
    Optional<UwApprovalStep> findFirstByCaseIdAndStepStatusOrderByStepOrderAsc(UUID caseId, UwApprovalStepStatus status);
}
