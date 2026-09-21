package com.project.smartinsurance.underwritingService.repository;

import com.project.smartinsurance.underwritingService.model.UnderwritingDecision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnderwritingDecisionRepository extends JpaRepository<UnderwritingDecision, UUID> {
    List<UnderwritingDecision> findByCaseIdOrderByCreatedAtDesc(UUID caseId);
    Optional<UnderwritingDecision> findFirstByCaseIdAndFinalDecisionTrueOrderByCreatedAtDesc(UUID caseId);
}
