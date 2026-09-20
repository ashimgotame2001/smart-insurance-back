package com.project.smartinsurance.billingService.service.impl;

import com.project.smartinsurance.billingService.dto.CollectionRequest;
import com.project.smartinsurance.billingService.dto.OnlinePaymentDto;
import com.project.smartinsurance.billingService.mapper.BillingMapper;
import com.project.smartinsurance.billingService.model.OnlinePayment;
import com.project.smartinsurance.billingService.model.OnlinePayment.OnlinePaymentStatus;
import com.project.smartinsurance.billingService.repository.OnlinePaymentRepository;
import com.project.smartinsurance.billingService.service.OnlinePaymentService;
import com.project.smartinsurance.billingService.service.PremiumBillingService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnlinePaymentServiceImpl implements OnlinePaymentService {

    private final OnlinePaymentRepository repository;
    private final BillingMapper mapper;
    private final PremiumBillingService premiumBillingService;

    @Override
    @Transactional
    public OnlinePaymentDto create(OnlinePaymentDto dto) {
        OnlinePayment entity = mapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        if (entity.getPaymentStatus() == null) {
            entity.setPaymentStatus(OnlinePaymentStatus.PENDING);
        }
        entity = repository.save(entity);
        if (entity.getPaymentStatus() == OnlinePaymentStatus.SUCCESS) {
            settleInstallment(entity);
        }
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public OnlinePaymentDto update(UUID id, OnlinePaymentDto dto) {
        OnlinePayment entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-003"));
        OnlinePaymentStatus before = entity.getPaymentStatus();
        mapper.updateEntity(dto, entity);
        entity = repository.save(entity);
        if (entity.getPaymentStatus() == OnlinePaymentStatus.SUCCESS
                && before != OnlinePaymentStatus.SUCCESS) {
            settleInstallment(entity);
        }
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        OnlinePayment entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-003"));
        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    @Override
    public OnlinePaymentDto findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new GlobalException("BIL-SER-003"));
    }

    @Override
    public List<OnlinePaymentDto> findAll() {
        return repository.findAllByStatus(Status.ACTIVE).stream()
                .map(mapper::toDto)
                .toList();
    }

    private void settleInstallment(OnlinePayment entity) {
        if (entity.getAmount() == null || entity.getAmount().signum() <= 0) return;
        try {
            CollectionRequest.AllocationLine line = null;
            if (entity.getInstallmentId() != null) {
                line = CollectionRequest.AllocationLine.builder()
                        .installmentId(entity.getInstallmentId())
                        .amount(entity.getAmount())
                        .build();
            }
            CollectionRequest req = CollectionRequest.builder()
                    .policyId(entity.getPolicyId())
                    .amount(entity.getAmount())
                    .paymentMode("ONLINE")
                    .paymentDate(entity.getPaymentDate() != null
                            ? entity.getPaymentDate().toLocalDate()
                            : LocalDate.now())
                    .referenceNo(entity.getTransactionId())
                    .description("Gateway settlement: " + entity.getGatewayName())
                    .allocations(line != null ? List.of(line) : null)
                    .build();
            premiumBillingService.collect(req);
            log.info("Online payment {} settled against installments", entity.getTransactionId());
        } catch (Exception e) {
            log.warn("Failed to settle online payment {}: {}", entity.getTransactionId(), e.getMessage());
        }
    }
}
