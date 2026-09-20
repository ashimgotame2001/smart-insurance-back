package com.project.smartinsurance.billingService.service;

import com.project.smartinsurance.billingService.dto.ReceiptDto;

import java.util.List;
import java.util.UUID;

public interface ReceiptService {
    ReceiptDto create(ReceiptDto dto);
    ReceiptDto update(UUID id, ReceiptDto dto);
    void delete(UUID id);
    ReceiptDto findById(UUID id);
    List<ReceiptDto> findAll();
}
