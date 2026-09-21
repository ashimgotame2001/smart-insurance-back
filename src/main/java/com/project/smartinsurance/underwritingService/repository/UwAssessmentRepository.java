package com.project.smartinsurance.underwritingService.repository;

import com.project.smartinsurance.underwritingService.model.UwAssessment;
import com.project.smartinsurance.underwritingService.model.enums.UwAssessmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UwAssessmentRepository extends JpaRepository<UwAssessment, UUID> {
    List<UwAssessment> findByCaseIdOrderByCreatedAtDesc(UUID caseId);
    List<UwAssessment> findByCaseIdAndAssessmentTypeOrderByCreatedAtDesc(UUID caseId, UwAssessmentType type);
    Page<UwAssessment> findByAssessmentTypeOrderByCreatedAtDesc(UwAssessmentType type, Pageable pageable);
}
