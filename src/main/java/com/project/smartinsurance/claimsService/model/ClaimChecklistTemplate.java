package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.claimsService.model.enums.ClaimType;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "claim_checklist_templates", uniqueConstraints = {
        @UniqueConstraint(name = "uk_claim_checklist_type_doc", columnNames = {"claim_type", "document_type"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimChecklistTemplate extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type", nullable = false)
    private ClaimType claimType;

    @Column(name = "document_type", nullable = false)
    private String documentType;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "required")
    @Builder.Default
    private Boolean required = true;
}
