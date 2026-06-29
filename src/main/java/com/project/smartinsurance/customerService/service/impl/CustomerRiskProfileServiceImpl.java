package com.project.smartinsurance.customerService.service.impl;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.customerService.dto.CustomerRiskProfileDto;
import com.project.smartinsurance.customerService.mapper.CustomerRiskProfileMapper;
import com.project.smartinsurance.customerService.model.Customer;
import com.project.smartinsurance.customerService.model.CustomerRiskProfile;
import com.project.smartinsurance.customerService.model.enums.RiskCategory;
import com.project.smartinsurance.customerService.repository.CustomerRepository;
import com.project.smartinsurance.customerService.repository.CustomerRiskProfileRepository;
import com.project.smartinsurance.customerService.service.CustomerRiskProfileService;
import com.project.smartinsurance.identityService.model.User;
import com.project.smartinsurance.identityService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerRiskProfileServiceImpl implements CustomerRiskProfileService {

    private final CustomerRiskProfileRepository repository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerRiskProfileMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerRiskProfileDto> getRiskProfilesByCustomerId(UUID customerId) {
        return repository.findByCustomerIdOrderByAssessmentDateDesc(customerId)
                .stream().map(mapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerRiskProfileDto getLatestRiskProfile(UUID customerId) {
        return repository.findTopByCustomerIdOrderByAssessmentDateDesc(customerId)
                .map(mapper::toDto).orElse(null);
    }

    @Override
    @Transactional
    public CustomerRiskProfileDto createRiskProfile(CustomerRiskProfileDto dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new GlobalException("CUS-001"));

        CustomerRiskProfile profile = new CustomerRiskProfile();
        profile.setCustomer(customer);
        profile.setRiskCategory(RiskCategory.valueOf(dto.getRiskCategory()));
        profile.setRiskScore(dto.getRiskScore());
        profile.setAssessmentDate(dto.getAssessmentDate());
        profile.setFactors(dto.getFactors());
        profile.setNotes(dto.getNotes());
        profile.setNextReviewDate(dto.getNextReviewDate());
        profile.setStatus(Status.ACTIVE);

        if (dto.getAssessedById() != null) {
            User assessor = userRepository.findById(dto.getAssessedById()).orElse(null);
            profile.setAssessedBy(assessor);
        }

        return mapper.toDto(repository.save(profile));
    }

    @Override
    @Transactional
    public CustomerRiskProfileDto updateRiskProfile(UUID id, CustomerRiskProfileDto dto) {
        CustomerRiskProfile profile = repository.findById(id)
                .orElseThrow(() -> new GlobalException("RP-001"));

        profile.setRiskCategory(RiskCategory.valueOf(dto.getRiskCategory()));
        profile.setRiskScore(dto.getRiskScore());
        profile.setAssessmentDate(dto.getAssessmentDate());
        profile.setFactors(dto.getFactors());
        profile.setNotes(dto.getNotes());
        profile.setNextReviewDate(dto.getNextReviewDate());

        if (dto.getAssessedById() != null) {
            User assessor = userRepository.findById(dto.getAssessedById()).orElse(null);
            profile.setAssessedBy(assessor);
        }

        return mapper.toDto(repository.save(profile));
    }

    @Override
    @Transactional
    public void deleteRiskProfile(UUID id) {
        CustomerRiskProfile profile = repository.findById(id)
                .orElseThrow(() -> new GlobalException("RP-001"));
        profile.setStatus(Status.INACTIVE);
        repository.save(profile);
    }
}
