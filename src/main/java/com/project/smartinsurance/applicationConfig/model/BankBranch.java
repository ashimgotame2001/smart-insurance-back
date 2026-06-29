package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "master_bank_branches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankBranch extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    private String address;
    private String city;
    private String phone;

    @Column(name = "swift_code")
    private String swiftCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;
}
