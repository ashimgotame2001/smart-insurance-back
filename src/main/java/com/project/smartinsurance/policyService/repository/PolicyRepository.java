package com.project.smartinsurance.policyService.repository;

import com.project.smartinsurance.policyService.model.Policy;
import com.project.smartinsurance.policyService.model.enums.PolicyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, UUID> {

    Optional<Policy> findByPolicyNumber(String policyNumber);

    Page<Policy> findByCustomerId(UUID customerId, Pageable pageable);

    Page<Policy> findByPolicyStatus(PolicyStatus policyStatus, Pageable pageable);

    Page<Policy> findByBranchId(UUID branchId, Pageable pageable);

    Page<Policy> findByEffectiveDateBetween(LocalDate start, LocalDate end, Pageable pageable);

    List<Policy> findByPolicyStatusAndExpiryDateBetween(PolicyStatus status, LocalDate start, LocalDate end);

    long countByPolicyStatus(PolicyStatus policyStatus);

    long countByBranchId(UUID branchId);

    long countByPolicyStatusAndBranchId(PolicyStatus policyStatus, UUID branchId);

    long countByCreatedAtGreaterThanEqual(LocalDateTime from);

    long countByBranchIdAndCreatedAtGreaterThanEqual(UUID branchId, LocalDateTime from);

    long countByPolicyStatusAndExpiryDateBetween(PolicyStatus status, LocalDate start, LocalDate end);

    long countByPolicyStatusAndBranchIdAndExpiryDateBetween(PolicyStatus status, UUID branchId, LocalDate start, LocalDate end);

    boolean existsByPolicyNumber(String policyNumber);

    @Query("SELECT COALESCE(SUM(p.totalPremium), 0) FROM Policy p WHERE p.policyStatus = :status")
    BigDecimal sumTotalPremiumByStatus(@Param("status") PolicyStatus status);

    @Query("SELECT COALESCE(SUM(p.totalPremium), 0) FROM Policy p WHERE p.policyStatus = :status AND p.branchId = :branchId")
    BigDecimal sumTotalPremiumByStatusAndBranch(@Param("status") PolicyStatus status, @Param("branchId") UUID branchId);

    @Query("SELECT COALESCE(SUM(p.totalPremium), 0) FROM Policy p WHERE p.policyStatus = :status AND p.issueDate >= :from AND p.issueDate <= :to")
    BigDecimal sumTotalPremiumByStatusAndIssueDateBetween(@Param("status") PolicyStatus status,
                                                          @Param("from") LocalDate from,
                                                          @Param("to") LocalDate to);

    @Query("SELECT COALESCE(SUM(p.totalPremium), 0) FROM Policy p WHERE p.policyStatus = :status AND p.branchId = :branchId AND p.issueDate >= :from AND p.issueDate <= :to")
    BigDecimal sumTotalPremiumByStatusAndBranchAndIssueDateBetween(@Param("status") PolicyStatus status,
                                                                   @Param("branchId") UUID branchId,
                                                                   @Param("from") LocalDate from,
                                                                   @Param("to") LocalDate to);

    @Query("SELECT p.branchId, COUNT(p), COALESCE(SUM(p.totalPremium), 0) FROM Policy p " +
           "WHERE p.policyStatus = :status AND p.branchId IS NOT NULL GROUP BY p.branchId ORDER BY SUM(p.totalPremium) DESC")
    List<Object[]> aggregatePremiumByBranch(@Param("status") PolicyStatus status);

    @Query("SELECT COALESCE(p.productCode, 'UNKNOWN'), COUNT(p), COALESCE(SUM(p.totalPremium), 0) FROM Policy p " +
           "WHERE p.policyStatus = :status GROUP BY p.productCode ORDER BY COUNT(p) DESC")
    List<Object[]> aggregateByProduct(@Param("status") PolicyStatus status);

    @Query("SELECT p FROM Policy p ORDER BY p.createdAt DESC")
    List<Policy> findRecent(Pageable pageable);

    @Query("SELECT p FROM Policy p WHERE p.branchId = :branchId ORDER BY p.createdAt DESC")
    List<Policy> findRecentByBranch(@Param("branchId") UUID branchId, Pageable pageable);
}
