package com.project.smartinsurance.billingService.service;

import com.project.smartinsurance.billingService.dto.InvoiceDto;
import com.project.smartinsurance.billingService.model.Invoice.InvoiceStatus;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {
    InvoiceDto create(InvoiceDto dto);
    InvoiceDto update(UUID id, InvoiceDto dto);
    void delete(UUID id);
    InvoiceDto findById(UUID id);
    List<InvoiceDto> findAll();
    List<InvoiceDto> findByStatus(InvoiceStatus status);
    InvoiceDto updateStatus(UUID id, InvoiceStatus status);
}
