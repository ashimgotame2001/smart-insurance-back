package com.project.smartinsurance.billingService.service;

import com.project.smartinsurance.billingService.dto.PaymentGatewayDto;

import java.util.List;
import java.util.UUID;

public interface PaymentGatewayService {
    PaymentGatewayDto create(PaymentGatewayDto dto);
    PaymentGatewayDto update(UUID id, PaymentGatewayDto dto);
    void delete(UUID id);
    PaymentGatewayDto findById(UUID id);
    List<PaymentGatewayDto> findAll();
    List<PaymentGatewayDto> findActive();
}
