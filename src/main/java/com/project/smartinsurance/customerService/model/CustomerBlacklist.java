package com.project.smartinsurance.customerService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.commonService.model.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_blacklist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBlacklist extends BaseEntity {

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "customer_code")
    private String customerCode;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_type")
    private String customerType;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "blacklisted_by")
    private String blacklistedBy;

    @Column(name = "blacklisted_at")
    private LocalDateTime blacklistedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
