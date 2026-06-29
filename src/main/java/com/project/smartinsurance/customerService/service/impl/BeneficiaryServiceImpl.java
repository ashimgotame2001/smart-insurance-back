package com.project.smartinsurance.customerService.service.impl;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.customerService.dto.BeneficiaryDto;
import com.project.smartinsurance.customerService.model.Beneficiary;
import com.project.smartinsurance.customerService.model.Customer;
import com.project.smartinsurance.customerService.repository.BeneficiaryRepository;
import com.project.smartinsurance.customerService.repository.CustomerRepository;
import com.project.smartinsurance.customerService.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public BeneficiaryDto createBeneficiary(UUID customerId, BeneficiaryDto dto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new GlobalException("CUS-001"));

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setCustomer(customer);
        beneficiary.setFirstName(dto.getFirstName());
        beneficiary.setLastName(dto.getLastName());
        beneficiary.setMiddleName(dto.getMiddleName());
        beneficiary.setDateOfBirth(dto.getDateOfBirth());
        beneficiary.setGender(dto.getGender());
        beneficiary.setRelationship(dto.getRelationship());
        beneficiary.setPhone(dto.getPhone());
        beneficiary.setEmail(dto.getEmail());
        beneficiary.setAddress(dto.getAddress());
        beneficiary.setBeneficiaryType(dto.getBeneficiaryType());
        beneficiary.setPercentage(dto.getPercentage());
        beneficiary.setPriority(dto.getPriority());

        return toDto(beneficiaryRepository.save(beneficiary));
    }

    @Override
    @Transactional
    public BeneficiaryDto updateBeneficiary(UUID id, BeneficiaryDto dto) {
        Beneficiary existing = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BEN-001"));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setMiddleName(dto.getMiddleName());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setGender(dto.getGender());
        existing.setRelationship(dto.getRelationship());
        existing.setPhone(dto.getPhone());
        existing.setEmail(dto.getEmail());
        existing.setAddress(dto.getAddress());
        existing.setBeneficiaryType(dto.getBeneficiaryType());
        existing.setPercentage(dto.getPercentage());
        existing.setPriority(dto.getPriority());

        return toDto(beneficiaryRepository.save(existing));
    }

    @Override
    public List<BeneficiaryDto> getBeneficiariesByCustomer(UUID customerId) {
        return beneficiaryRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public BeneficiaryDto getBeneficiaryById(UUID id) {
        return beneficiaryRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new GlobalException("BEN-001"));
    }

    @Override
    @Transactional
    public void deleteBeneficiary(UUID id) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BEN-001"));
        beneficiary.setStatus(Status.DELETED);
        beneficiaryRepository.save(beneficiary);
    }

    private BeneficiaryDto toDto(Beneficiary b) {
        return BeneficiaryDto.builder()
                .id(b.getId())
                .status(b.getStatus())
                .customerId(b.getCustomer().getId())
                .firstName(b.getFirstName())
                .lastName(b.getLastName())
                .middleName(b.getMiddleName())
                .dateOfBirth(b.getDateOfBirth())
                .gender(b.getGender())
                .relationship(b.getRelationship())
                .phone(b.getPhone())
                .email(b.getEmail())
                .address(b.getAddress())
                .beneficiaryType(b.getBeneficiaryType())
                .percentage(b.getPercentage())
                .priority(b.getPriority())
                .createdAt(b.getCreatedAt())
                .build();
    }
}
