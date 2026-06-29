package com.project.smartinsurance.customerService.service.impl;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.customerService.dto.CustomerBlacklistDto;
import com.project.smartinsurance.customerService.model.CustomerBlacklist;
import com.project.smartinsurance.customerService.repository.CustomerBlacklistRepository;
import com.project.smartinsurance.customerService.service.CustomerBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerBlacklistServiceImpl implements CustomerBlacklistService {

    private final CustomerBlacklistRepository repository;

    @Override
    @Transactional(readOnly = true)
    public PagedData<CustomerBlacklistDto> getBlacklistedCustomers(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<CustomerBlacklist> result = repository.findAll(PageRequest.of(page, size, sort));
        List<CustomerBlacklistDto> content = result.stream().map(this::toDto).collect(Collectors.toList());
        return PagedData.<CustomerBlacklistDto>builder()
                .content(content).page(result.getNumber()).size(result.getSize())
                .totalElements(result.getTotalElements()).totalPages(result.getTotalPages()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerBlacklistDto> searchBlacklist(String q) {
        return repository.findByCustomerCodeContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(q, q)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerBlacklistDto blacklistCustomer(CustomerBlacklistDto dto) {
        if (repository.existsByCustomerId(dto.getCustomerId())) {
            throw new GlobalException("BL-001");
        }

        CustomerBlacklist entry = new CustomerBlacklist();
        entry.setCustomerId(dto.getCustomerId());
        entry.setCustomerCode(dto.getCustomerCode());
        entry.setCustomerName(dto.getCustomerName());
        entry.setCustomerType(dto.getCustomerType());
        entry.setReason(dto.getReason());
        entry.setBlacklistedBy(dto.getBlacklistedBy());
        entry.setBlacklistedAt(LocalDateTime.now());
        entry.setStatus(Status.ACTIVE);

        return toDto(repository.save(entry));
    }

    @Override
    @Transactional
    public CustomerBlacklistDto updateBlacklist(UUID id, CustomerBlacklistDto dto) {
        CustomerBlacklist entry = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BL-002", id.toString()));

        entry.setReason(dto.getReason());
        entry.setCustomerCode(dto.getCustomerCode());
        entry.setCustomerName(dto.getCustomerName());
        entry.setCustomerType(dto.getCustomerType());

        return toDto(repository.save(entry));
    }

    @Override
    @Transactional
    public void removeFromBlacklist(UUID id) {
        CustomerBlacklist entry = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BL-002", id.toString()));
        entry.setStatus(Status.INACTIVE);
        repository.save(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCustomerBlacklisted(UUID customerId) {
        return repository.findByCustomerId(customerId)
                .filter(e -> e.getStatus() == Status.ACTIVE)
                .isPresent();
    }

    private CustomerBlacklistDto toDto(CustomerBlacklist entry) {
        return CustomerBlacklistDto.builder()
                .id(entry.getId())
                .customerId(entry.getCustomerId())
                .customerCode(entry.getCustomerCode())
                .customerName(entry.getCustomerName())
                .customerType(entry.getCustomerType())
                .reason(entry.getReason())
                .blacklistedBy(entry.getBlacklistedBy())
                .blacklistedAt(entry.getBlacklistedAt())
                .status(entry.getStatus() != null ? entry.getStatus().name() : null)
                .build();
    }
}
