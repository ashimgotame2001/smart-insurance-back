package com.project.smartinsurance.customerService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.commonService.model.Document;
import com.project.smartinsurance.identityService.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_kyc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KYC extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id", nullable = false)
    private IdentityTypeConfig documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "front_document_id")
    private Document frontDocument;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "back_document_id")
    private Document backDocument;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "verification_status", nullable = false)
    private String verificationStatus;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
}
