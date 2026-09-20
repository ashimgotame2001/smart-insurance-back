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
@Table(name = "bil_payment_gateways")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentGateway extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 200)
    private String provider;

    @Column(length = 200)
    private String merchantId;

    @Column(length = 500)
    private String apiKey;

    @Column(length = 500)
    private String secretKey;

    @Column(length = 500)
    private String webhookUrl;

    @Column(length = 20)
    private String gatewayType;

    @Column(nullable = false)
    private Boolean active;
}
