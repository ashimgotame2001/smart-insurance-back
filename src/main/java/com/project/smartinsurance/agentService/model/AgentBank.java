package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "agent_banks", indexes = {
        @Index(name = "idx_account_number", columnList = "account_number"),
        @Index(name = "idx_pan_number", columnList = "pan_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentBank extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Column(name = "bank_id")
    private UUID bankId;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "swift_code")
    private String swiftCode;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "tax_registration_number")
    private String taxRegistrationNumber;

    @Builder.Default
    @Column(name = "is_primary")
    private Boolean isPrimary = false;
}
