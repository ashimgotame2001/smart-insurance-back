package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.ReinsuranceCompanyDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.ReinsuranceCompany;
import com.project.smartinsurance.applicationConfig.repository.ReinsuranceCompanyRepository;
import com.project.smartinsurance.applicationConfig.service.ReinsuranceCompanyService;
import com.project.smartinsurance.applicationConfig.validation.MasterDataValidationService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReinsuranceCompanyServiceImpl implements ReinsuranceCompanyService {

    private final ReinsuranceCompanyRepository reinsuranceCompanyRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public ReinsuranceCompanyDto createReinsuranceCompany(ReinsuranceCompanyDto dto) {
        validationService.validateUniqueCode(reinsuranceCompanyRepository, dto.getCode());
        ReinsuranceCompany entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(reinsuranceCompanyRepository.save(entity));
    }

    @Override
    @Transactional
    public ReinsuranceCompanyDto updateReinsuranceCompany(UUID id, ReinsuranceCompanyDto dto) {
        ReinsuranceCompany entity = reinsuranceCompanyRepository.findById(id)
                .orElseThrow(() -> new GlobalException("RIN-001"));
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setCountry(dto.getCountry());
        entity.setRating(dto.getRating());
        entity.setContactEmail(dto.getContactEmail());
        entity.setContactPhone(dto.getContactPhone());
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(reinsuranceCompanyRepository.save(entity));
    }

    @Override
    public ReinsuranceCompanyDto getReinsuranceCompanyById(UUID id) {
        return reinsuranceCompanyRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("RIN-001"));
    }

    @Override
    public List<ReinsuranceCompanyDto> getAllReinsuranceCompanies() {
        return reinsuranceCompanyRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteReinsuranceCompany(UUID id) {
        ReinsuranceCompany entity = reinsuranceCompanyRepository.findById(id)
                .orElseThrow(() -> new GlobalException("RIN-001"));
        entity.setStatus(Status.DELETED);
        reinsuranceCompanyRepository.save(entity);
    }
}
