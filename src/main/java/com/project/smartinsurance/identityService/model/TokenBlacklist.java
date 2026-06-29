package com.project.smartinsurance.identityService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "token_blacklist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBlacklist extends BaseEntity {

    @Column(nullable = false, unique = true, length = 2000)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}
