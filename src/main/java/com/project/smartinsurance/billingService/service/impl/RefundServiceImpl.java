package com.project.smartinsurance.billingService.service.impl;

import com.project.smartinsurance.billingService.dto.RefundDto;
import com.project.smartinsurance.billingService.mapper.BillingMapper;
import com.project.smartinsurance.billingService.model.Refund;
import com.project.smartinsurance.billingService.repository.RefundRepository;
import com.project.smartinsurance.billingService.service.RefundService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository repository;
    private final BillingMapper mapper;

    @Override
    @Transactional
    public RefundDto create(RefundDto dto) {
        Refund entity = mapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public RefundDto update(UUID id, RefundDto dto) {
        Refund entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-006"));
        mapper.updateEntity(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Refund entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-006"));
        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    @Override
    public RefundDto findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new GlobalException("BIL-SER-006"));
    }

    @Override
    public List<RefundDto> findAll() {
        return repository.findAllByStatus(Status.ACTIVE).stream()
                .map(mapper::toDto)
                .toList();
    }
}
