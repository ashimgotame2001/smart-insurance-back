package com.project.smartinsurance.policyService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "policy_audit_trails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyAuditTrail extends BaseEntity {

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "action_description")
    private String actionDescription;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "actor")
    private String actor;

    @Column(name = "actor_role")
    private String actorRole;

    @Column(name = "correlation_id")
    private String correlationId;
}
