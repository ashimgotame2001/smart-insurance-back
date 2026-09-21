package com.project.smartinsurance.underwritingService.repository;

import com.project.smartinsurance.underwritingService.model.UnderwritingCase;
import com.project.smartinsurance.underwritingService.model.enums.UwCaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UnderwritingCaseRepository extends JpaRepository<UnderwritingCase, UUID> {
    Optional<UnderwritingCase> findByPolicyId(UUID policyId);
    Optional<UnderwritingCase> findByCaseNumber(String caseNumber);
    long countByCaseStatus(UwCaseStatus status);

    @Query("""
            SELECT c FROM UnderwritingCase c
            WHERE (:status IS NULL OR c.caseStatus = :status)
              AND (:assignee IS NULL OR LOWER(c.assignee) = LOWER(:assignee))
              AND (:lob IS NULL OR LOWER(c.lineOfBusiness) = LOWER(:lob))
              AND (:search IS NULL OR LOWER(c.caseNumber) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(c.policyNumber) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(c.customerName) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    Page<UnderwritingCase> search(
            @Param("status") UwCaseStatus status,
            @Param("assignee") String assignee,
            @Param("lob") String lob,
            @Param("search") String search,
            Pageable pageable);
}
