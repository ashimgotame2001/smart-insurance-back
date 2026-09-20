package com.project.smartinsurance.billingService.service.impl;

import com.project.smartinsurance.billingService.dto.InvoiceDto;
import com.project.smartinsurance.billingService.mapper.BillingMapper;
import com.project.smartinsurance.billingService.model.Invoice;
import com.project.smartinsurance.billingService.model.Invoice.InvoiceStatus;
import com.project.smartinsurance.billingService.repository.InvoiceRepository;
import com.project.smartinsurance.billingService.service.InvoiceService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;
    private final BillingMapper mapper;

    @Override
    @Transactional
    public InvoiceDto create(InvoiceDto dto) {
        Invoice entity = mapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        if (entity.getInvoiceStatus() == null) {
            entity.setInvoiceStatus(InvoiceStatus.DRAFT);
        }
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public InvoiceDto update(UUID id, InvoiceDto dto) {
        Invoice entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-002"));
        mapper.updateEntity(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Invoice entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-002"));
        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    @Override
    public InvoiceDto findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new GlobalException("BIL-SER-002"));
    }

    @Override
    public List<InvoiceDto> findAll() {
        return repository.findAllByStatus(Status.ACTIVE).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<InvoiceDto> findByStatus(InvoiceStatus status) {
        return repository.findByStatusAndInvoiceStatus(Status.ACTIVE, status).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public InvoiceDto updateStatus(UUID id, InvoiceStatus status) {
        Invoice entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-002"));
        entity.setInvoiceStatus(status);
        return mapper.toDto(repository.save(entity));
    }
}
