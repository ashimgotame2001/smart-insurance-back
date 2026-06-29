package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.applicationConfig.model.enums.BranchStatus;
import com.project.smartinsurance.applicationConfig.model.enums.BranchType;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "branch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchEntity extends BaseEntity {

    @Column(name = "branch_code", unique = true, nullable = false)
    private String branchCode;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @Enumerated(EnumType.STRING)
    @Column(name = "branch_type", nullable = false)
    private BranchType branchType;

    @Enumerated(EnumType.STRING)
    @Column(name = "branch_status", nullable = false)
    private BranchStatus branchStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_branch_id")
    private BranchEntity parentBranch;

    @OneToMany(mappedBy = "parentBranch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BranchEntity> childBranches;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "is_head_office")
    private boolean isHeadOffice;

}
