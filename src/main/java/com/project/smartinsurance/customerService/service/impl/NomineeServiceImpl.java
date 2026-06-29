package com.project.smartinsurance.customerService.service.impl;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.customerService.dto.NomineeDto;
import com.project.smartinsurance.customerService.model.Customer;
import com.project.smartinsurance.customerService.model.Nominee;
import com.project.smartinsurance.customerService.repository.CustomerRepository;
import com.project.smartinsurance.customerService.repository.NomineeRepository;
import com.project.smartinsurance.customerService.service.NomineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NomineeServiceImpl implements NomineeService {

    private final NomineeRepository nomineeRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public NomineeDto createNominee(UUID customerId, NomineeDto dto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new GlobalException("CUS-001"));

        Nominee nominee = new Nominee();
        nominee.setCustomer(customer);
        nominee.setFirstName(dto.getFirstName());
        nominee.setLastName(dto.getLastName());
        nominee.setMiddleName(dto.getMiddleName());
        nominee.setDateOfBirth(dto.getDateOfBirth());
        nominee.setGender(dto.getGender());
        nominee.setRelationship(dto.getRelationship());
        nominee.setPhone(dto.getPhone());
        nominee.setEmail(dto.getEmail());
        nominee.setAddress(dto.getAddress());
        nominee.setNomineeType(dto.getNomineeType());
        nominee.setPercentage(dto.getPercentage());

        return toDto(nomineeRepository.save(nominee));
    }

    @Override
    @Transactional
    public NomineeDto updateNominee(UUID id, NomineeDto dto) {
        Nominee existing = nomineeRepository.findById(id)
                .orElseThrow(() -> new GlobalException("NOM-001"));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setMiddleName(dto.getMiddleName());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setGender(dto.getGender());
        existing.setRelationship(dto.getRelationship());
        existing.setPhone(dto.getPhone());
        existing.setEmail(dto.getEmail());
        existing.setAddress(dto.getAddress());
        existing.setNomineeType(dto.getNomineeType());
        existing.setPercentage(dto.getPercentage());

        return toDto(nomineeRepository.save(existing));
    }

    @Override
    public List<NomineeDto> getNomineesByCustomer(UUID customerId) {
        return nomineeRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public NomineeDto getNomineeById(UUID id) {
        return nomineeRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new GlobalException("NOM-001"));
    }

    @Override
    @Transactional
    public void deleteNominee(UUID id) {
        Nominee nominee = nomineeRepository.findById(id)
                .orElseThrow(() -> new GlobalException("NOM-001"));
        nominee.setStatus(Status.DELETED);
        nomineeRepository.save(nominee);
    }

    private NomineeDto toDto(Nominee n) {
        return NomineeDto.builder()
                .id(n.getId())
                .status(n.getStatus())
                .customerId(n.getCustomer().getId())
                .firstName(n.getFirstName())
                .lastName(n.getLastName())
                .middleName(n.getMiddleName())
                .dateOfBirth(n.getDateOfBirth())
                .gender(n.getGender())
                .relationship(n.getRelationship())
                .phone(n.getPhone())
                .email(n.getEmail())
                .address(n.getAddress())
                .nomineeType(n.getNomineeType())
                .percentage(n.getPercentage())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
