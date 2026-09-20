package com.project.smartinsurance.billingService.service.impl;

import com.project.smartinsurance.billingService.dto.ReceiptDto;
import com.project.smartinsurance.billingService.mapper.BillingMapper;
import com.project.smartinsurance.billingService.model.Receipt;
import com.project.smartinsurance.billingService.repository.ReceiptRepository;
import com.project.smartinsurance.billingService.service.ReceiptService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository repository;
    private final BillingMapper mapper;

    @Override
    @Transactional
    public ReceiptDto create(ReceiptDto dto) {
        Receipt entity = mapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public ReceiptDto update(UUID id, ReceiptDto dto) {
        Receipt entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-005"));
        mapper.updateEntity(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Receipt entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-005"));
        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    @Override
    public ReceiptDto findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new GlobalException("BIL-SER-005"));
    }

    @Override
    public List<ReceiptDto> findAll() {
        return repository.findAllByStatus(Status.ACTIVE).stream()
                .map(mapper::toDto)
                .toList();
    }
}
