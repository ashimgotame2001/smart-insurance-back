package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.AgentPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentPerformanceRepository extends JpaRepository<AgentPerformance, UUID> {

    Optional<AgentPerformance> findByAgentId(UUID agentId);

    @Query("SELECT COALESCE(SUM(p.commissionEarned), 0) FROM AgentPerformance p")
    BigDecimal sumCommissionEarned();

    @Query("SELECT COALESCE(SUM(p.totalPoliciesSold), 0) FROM AgentPerformance p")
    Long sumPoliciesSold();

    @Query("SELECT COALESCE(SUM(p.totalPremiumCollected), 0) FROM AgentPerformance p")
    BigDecimal sumPremiumCollected();

    @Query("SELECT COALESCE(AVG(p.persistencyRatio), 0) FROM AgentPerformance p WHERE p.persistencyRatio IS NOT NULL")
    BigDecimal avgPersistencyRatio();

    @Query("SELECT COALESCE(AVG(p.claimRatio), 0) FROM AgentPerformance p WHERE p.claimRatio IS NOT NULL")
    BigDecimal avgClaimRatio();

    @Query("SELECT p FROM AgentPerformance p LEFT JOIN FETCH p.agent ORDER BY COALESCE(p.totalPremiumCollected, 0) DESC")
    List<AgentPerformance> findTopByPremium();
}
