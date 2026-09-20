package com.project.smartinsurance.billingService.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentGatewayDto {
    private UUID id;
    private String name;
    private String provider;
    private String merchantId;
    private String apiKey;
    private String secretKey;
    private String webhookUrl;
    private String gatewayType;
    private Boolean active;
    private Status status;
}
