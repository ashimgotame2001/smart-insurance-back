package com.project.smartinsurance.billingService.service;

import com.project.smartinsurance.billingService.dto.OnlinePaymentDto;

import java.util.List;
import java.util.UUID;

public interface OnlinePaymentService {
    OnlinePaymentDto create(OnlinePaymentDto dto);
    OnlinePaymentDto update(UUID id, OnlinePaymentDto dto);
    void delete(UUID id);
    OnlinePaymentDto findById(UUID id);
    List<OnlinePaymentDto> findAll();
}
