package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.claimsService.model.enums.ClaimDocumentStatus;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "claim_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimDocument extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_id")
    private UUID documentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_status")
    private ClaimDocumentStatus documentStatus;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @Column(name = "required")
    @Builder.Default
    private Boolean required = false;
}
