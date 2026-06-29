package com.project.smartinsurance.identityService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mfa_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MfaPolicy extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = false;

    @Column(name = "issuer_name")
    private String issuerName;

    @Column(name = "code_length")
    @Builder.Default
    private Integer codeLength = 6;

    @Column(name = "time_step_seconds")
    @Builder.Default
    private Integer timeStepSeconds = 30;

    @Column(name = "enforce_for_all_users")
    @Builder.Default
    private Boolean enforceForAllUsers = false;

    @Column(name = "is_default")
    @Builder.Default
    private Boolean isDefault = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private UserGroup userGroup;
}
