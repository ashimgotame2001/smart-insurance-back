package com.project.smartinsurance.billingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bil_dunning_notices")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DunningNotice extends BaseEntity {

    @Column(name = "policy_id")
    private UUID policyId;

    @Column(name = "policy_number", length = 50)
    private String policyNumber;

    @Column(name = "installment_id")
    private UUID installmentId;

    @Column(name = "dunning_level")
    private Integer dunningLevel;

    @Column(length = 30)
    private String channel;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(length = 500)
    private String message;
}
