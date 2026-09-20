package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.agentService.model.enums.AgentDocumentType;
import com.project.smartinsurance.agentService.model.enums.VerificationStatus;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "agent_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentDocument extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private AgentDocumentType documentType;

    @Column(name = "storage_path")
    private String storagePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @Column(name = "uploaded_date")
    private LocalDateTime uploadedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private VerificationStatus verificationStatus;

    @Column(name = "verified_by")
    private String verifiedBy;

    @Column(name = "verified_date")
    private LocalDateTime verifiedDate;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "document_version")
    private Integer documentVersion;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;
}
