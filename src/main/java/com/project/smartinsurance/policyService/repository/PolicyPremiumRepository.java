package com.project.smartinsurance.policyService.repository;

import com.project.smartinsurance.policyService.model.PolicyPremium;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyPremiumRepository extends JpaRepository<PolicyPremium, UUID> {

    List<PolicyPremium> findByPolicyId(UUID policyId);

    List<PolicyPremium> findByPolicyIdOrderByDueDateAsc(UUID policyId);

    @Query("SELECT p FROM PolicyPremium p WHERE " +
           "(p.paymentStatus IS NULL OR UPPER(p.paymentStatus) IN ('UNPAID','PARTIAL','PENDING','DUE')) " +
           "AND (p.balanceAmount IS NULL OR p.balanceAmount > 0 OR (p.paidAmount IS NULL AND p.payableAmount IS NOT NULL)) " +
           "ORDER BY p.dueDate ASC")
    List<PolicyPremium> findOutstanding(Pageable pageable);

    @Query("SELECT p FROM PolicyPremium p WHERE p.dueDate <= :asOf " +
           "AND (p.paymentStatus IS NULL OR UPPER(p.paymentStatus) IN ('UNPAID','PARTIAL','PENDING','DUE')) " +
           "AND (p.balanceAmount IS NULL OR p.balanceAmount > 0 OR (p.paidAmount IS NULL AND COALESCE(p.payableAmount, p.totalPremium) > 0)) " +
           "ORDER BY p.dueDate ASC")
    List<PolicyPremium> findDueOnOrBefore(@Param("asOf") LocalDate asOf, Pageable pageable);

    long countByDueDateLessThanEqualAndPaymentStatusIgnoreCase(LocalDate dueDate, String paymentStatus);

    @Query("SELECT COUNT(p) FROM PolicyPremium p WHERE p.dueDate = :today " +
           "AND (p.paymentStatus IS NULL OR UPPER(p.paymentStatus) IN ('UNPAID','PARTIAL','PENDING','DUE'))")
    long countDueToday(@Param("today") LocalDate today);

    @Query("SELECT COUNT(p) FROM PolicyPremium p WHERE p.dueDate < :today " +
           "AND (p.paymentStatus IS NULL OR UPPER(p.paymentStatus) IN ('UNPAID','PARTIAL','PENDING','DUE'))")
    long countOverdue(@Param("today") LocalDate today);

    @Query("SELECT COALESCE(SUM(COALESCE(p.balanceAmount, COALESCE(p.payableAmount, p.totalPremium))), 0) FROM PolicyPremium p WHERE " +
           "(p.paymentStatus IS NULL OR UPPER(p.paymentStatus) IN ('UNPAID','PARTIAL','PENDING','DUE'))")
    java.math.BigDecimal sumOutstanding();
}
