package com.project.smartinsurance.customerService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.identityService.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer_timeline")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTimelineEntry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "activity_type", nullable = false)
    private String activityType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "reference_id")
    private String referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
}
