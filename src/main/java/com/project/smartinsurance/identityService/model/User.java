package com.project.smartinsurance.identityService.model;

import com.project.smartinsurance.applicationConfig.model.BranchEntity;
import com.project.smartinsurance.applicationConfig.model.CompanyProfileEntity;
import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.commonService.model.Document;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "application_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_pic_id")
    private Document profilePic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyProfileEntity company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private BranchEntity branch;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private UserGroup userGroup;

    // Access Policies
    @Column(name = "login_start_time")
    private LocalTime loginStartTime;

    @Column(name = "login_end_time")
    private LocalTime loginEndTime;

    @ElementCollection
    @CollectionTable(name = "user_allowed_days", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "day_of_week")
    private Set<Integer> allowedDaysOfWeek;

    @Column(name = "is_account_non_locked")
    private boolean isAccountNonLocked = true;

    @Column(name = "failed_login_attempts")
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column(name = "locked_until")
    private java.time.LocalDateTime lockedUntil;

    @Column(name = "is_enabled")
    private boolean isEnabled = true;

    @Column(name = "force_password_change")
    private boolean forcePasswordChange = false;

    @Column(name = "mfa_secret")
    private String mfaSecret;

    @Column(name = "mfa_enabled")
    @Builder.Default
    private Boolean mfaEnabled = false;
}
