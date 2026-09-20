package com.project.smartinsurance.billingService.service.impl;

import com.project.smartinsurance.billingService.dto.PaymentGatewayDto;
import com.project.smartinsurance.billingService.mapper.BillingMapper;
import com.project.smartinsurance.billingService.model.PaymentGateway;
import com.project.smartinsurance.billingService.repository.PaymentGatewayRepository;
import com.project.smartinsurance.billingService.service.PaymentGatewayService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentGatewayServiceImpl implements PaymentGatewayService {

    private final PaymentGatewayRepository repository;
    private final BillingMapper mapper;

    @Override
    @Transactional
    public PaymentGatewayDto create(PaymentGatewayDto dto) {
        PaymentGateway entity = mapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public PaymentGatewayDto update(UUID id, PaymentGatewayDto dto) {
        PaymentGateway entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-004"));
        mapper.updateEntity(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        PaymentGateway entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-004"));
        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    @Override
    public PaymentGatewayDto findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new GlobalException("BIL-SER-004"));
    }

    @Override
    public List<PaymentGatewayDto> findAll() {
        return repository.findAllByStatus(Status.ACTIVE).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<PaymentGatewayDto> findActive() {
        return repository.findByActiveTrueAndStatus(Status.ACTIVE).stream()
                .map(mapper::toDto)
                .toList();
    }
}
