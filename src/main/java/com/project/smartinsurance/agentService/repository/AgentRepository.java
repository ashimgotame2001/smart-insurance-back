package com.project.smartinsurance.agentService.repository;

import com.project.smartinsurance.agentService.model.Agent;
import com.project.smartinsurance.agentService.model.enums.AgentStatus;
import com.project.smartinsurance.agentService.model.enums.AgentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID> {

    Optional<Agent> findByAgentCodeAndDeletedFalse(String agentCode);

    Optional<Agent> findByIdAndDeletedFalse(UUID id);

    boolean existsByAgentCode(String agentCode);

    boolean existsByCitizenshipNumber(String citizenshipNumber);

    Page<Agent> findByDeletedFalse(Pageable pageable);

    List<Agent> findByReportingManagerIdAndDeletedFalse(UUID managerId);

    long countByDeletedFalse();

    long countByDeletedFalseAndAgentStatus(AgentStatus agentStatus);

    long countByDeletedFalseAndBranchId(UUID branchId);

    @Query("SELECT a FROM Agent a WHERE a.deleted = false AND " +
           "(:code IS NULL OR a.agentCode LIKE %:code%) AND " +
           "(:name IS NULL OR LOWER(a.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:agentType IS NULL OR a.agentType = :agentType) AND " +
           "(:agentStatus IS NULL OR a.agentStatus = :agentStatus) AND " +
           "(:branchId IS NULL OR a.branchId = :branchId) AND " +
           "(:citizenshipNumber IS NULL OR a.citizenshipNumber LIKE %:citizenshipNumber%)")
    Page<Agent> searchAgents(@Param("code") String code,
                            @Param("name") String name,
                            @Param("agentType") AgentType agentType,
                            @Param("agentStatus") AgentStatus agentStatus,
                            @Param("branchId") UUID branchId,
                            @Param("citizenshipNumber") String citizenshipNumber,
                            Pageable pageable);
}
