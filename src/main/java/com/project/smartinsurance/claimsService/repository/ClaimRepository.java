package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.Claim;
import com.project.smartinsurance.claimsService.model.enums.ClaimStatus;
import com.project.smartinsurance.claimsService.model.enums.ClaimType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, UUID>, JpaSpecificationExecutor<Claim> {

    Optional<Claim> findByClaimNumberAndDeletedFalse(String claimNumber);

    Optional<Claim> findByIdAndDeletedFalse(UUID id);

    boolean existsByPolicyIdAndClaimTypeAndLossDateBetweenAndDeletedFalse(
            UUID policyId, ClaimType claimType, LocalDate from, LocalDate to);

    Page<Claim> findByDeletedFalse(Pageable pageable);

    long countByClaimStatusInAndDeletedFalse(Collection<ClaimStatus> statuses);

    long countByClaimStatusAndDeletedFalse(ClaimStatus status);

    long countByFraudFlagTrueAndDeletedFalseAndClaimStatusNotIn(Collection<ClaimStatus> statuses);

    long countByDeletedFalseAndCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    long countByDeletedFalseAndClaimStatusInAndCreatedAtBefore(Collection<ClaimStatus> statuses, LocalDateTime before);

    long countByDeletedFalseAndClaimStatusInAndCreatedAtBetween(
            Collection<ClaimStatus> statuses, LocalDateTime from, LocalDateTime to);

    @Query("SELECT COALESCE(SUM(c.paidAmount), 0) FROM Claim c WHERE c.deleted = false AND c.paidAmount IS NOT NULL " +
           "AND c.updatedAt >= :from AND c.claimStatus IN :statuses")
    BigDecimal sumPaidAmountYtd(@Param("from") LocalDateTime from, @Param("statuses") Collection<ClaimStatus> statuses);

    @Query("SELECT c FROM Claim c WHERE c.deleted = false AND c.claimStatus IN :settledStatuses " +
           "AND c.settledAmount IS NOT NULL AND c.intimatedAt IS NOT NULL")
    List<Claim> findSettledForAvgDays(@Param("settledStatuses") Collection<ClaimStatus> settledStatuses);

    long countByDeletedFalseAndClaimNumberStartingWith(String prefix);
}
