package com.project.smartinsurance.billingService.service;

import com.project.smartinsurance.billingService.dto.RefundDto;

import java.util.List;
import java.util.UUID;

public interface RefundService {
    RefundDto create(RefundDto dto);
    RefundDto update(UUID id, RefundDto dto);
    void delete(UUID id);
    RefundDto findById(UUID id);
    List<RefundDto> findAll();
}
