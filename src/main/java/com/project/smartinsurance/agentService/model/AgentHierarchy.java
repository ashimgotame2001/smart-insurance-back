package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "agent_hierarchies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentHierarchy extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false, unique = true)
    private Agent agent;

    @Column(name = "parent_agent_id")
    private UUID parentAgentId;

    @Column(name = "agency_manager_id")
    private UUID agencyManagerId;

    @Column(name = "senior_agent_id")
    private UUID seniorAgentId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "hierarchy_level")
    private Integer hierarchyLevel;
}
