package com.project.smartinsurance.billingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bil_bank_accounts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccount extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String accountName;

    @Column(nullable = false, length = 50, unique = true)
    private String accountNumber;

    @Column(nullable = false, length = 200)
    private String bankName;

    @Column(length = 200)
    private String branchName;

    @Column(length = 50)
    private String swiftCode;

    @Column(length = 20)
    private String currency;

    @Column(nullable = false, length = 200)
    private String accountHolder;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
