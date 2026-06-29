package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.applicationConfig.dto.CompanyProfileDto;
import com.project.smartinsurance.applicationConfig.mapper.CompanyProfileMapper;
import com.project.smartinsurance.applicationConfig.model.CompanyProfileEntity;
import com.project.smartinsurance.applicationConfig.model.enums.CompanyStatus;
import com.project.smartinsurance.applicationConfig.repository.CompanyProfileRepository;
import com.project.smartinsurance.applicationConfig.service.CompanyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyProfileServiceImpl implements CompanyProfileService {

    private final CompanyProfileRepository companyProfileRepository;
    private final CompanyProfileMapper mapper;

    @Override
    @Transactional
    public CompanyProfileDto createCompanyProfile(CompanyProfileDto dto) {
        if (companyProfileRepository.count() > 0) {
            List<CompanyProfileEntity> existingProfiles = companyProfileRepository.findAll();
            CompanyProfileEntity template = existingProfiles.get(0);
            if ("TEMP_CODE".equals(template.getCompanyCode())) {
                if ("TEMP_CODE".equals(dto.getCompanyCode()) || dto.getCompanyCode() == null) {
                    throw new GlobalException("CMP-003");
                }
                template.setCompanyCode(dto.getCompanyCode());
                template.setCompanyName(dto.getCompanyName());
                template.setCompanyShortName(dto.getCompanyShortName());
                template.setRegistrationNumber(dto.getRegistrationNumber());
                template.setTaxIdentificationNumber(dto.getTaxIdentificationNumber());
                template.setLicenseNumber(dto.getLicenseNumber());
                template.setWebsiteUrl(dto.getWebsiteUrl());
                template.setEmail(dto.getEmail());
                template.setPhoneNumber(dto.getPhoneNumber());
                template.setSupportEmail(dto.getSupportEmail());
                template.setSupportPhone(dto.getSupportPhone());
                template.setAddressLine1(dto.getAddressLine1());
                template.setAddressLine2(dto.getAddressLine2());
                template.setCity(dto.getCity());
                template.setState(dto.getState());
                template.setCountry(dto.getCountry());
                template.setPostalCode(dto.getPostalCode());
                template.setLogoUrl(dto.getLogoUrl());
                template.setFaviconUrl(dto.getFaviconUrl());
                template.setPrimaryColor(dto.getPrimaryColor());
                template.setSecondaryColor(dto.getSecondaryColor());
                template.setCurrencyCode(dto.getCurrencyCode());
                template.setTimezone(dto.getTimezone());
                template.setBusinessType(dto.getBusinessType());
                if (dto.getCompanyStatus() != null) {
                    template.setCompanyStatus(dto.getCompanyStatus());
                } else {
                    template.setCompanyStatus(CompanyStatus.ACTIVE);
                }
                template.setEstablishedDate(dto.getEstablishedDate());
                template.setRemarks(dto.getRemarks());
                template.setStatus(Status.ACTIVE);
                return mapper.toDto(companyProfileRepository.save(template));
            }
            throw new GlobalException("CMP-002");
        }
        CompanyProfileEntity entity = mapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        if (entity.getCompanyStatus() == null) {
            entity.setCompanyStatus(CompanyStatus.ACTIVE);
        }
        return mapper.toDto(companyProfileRepository.save(entity));
    }

    @Override
    @Transactional
    public CompanyProfileDto updateCompanyProfile(UUID id, CompanyProfileDto dto) {
        CompanyProfileEntity existing = companyProfileRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CMP-001"));

        String currentCode = existing.getCompanyCode();
        if (!"TEMP_CODE".equals(currentCode) && "TEMP_CODE".equals(dto.getCompanyCode())) {
            throw new GlobalException("CMP-003");
        }

        existing.setCompanyName(dto.getCompanyName());
        existing.setCompanyCode(dto.getCompanyCode());
        existing.setCompanyShortName(dto.getCompanyShortName());
        existing.setRegistrationNumber(dto.getRegistrationNumber());
        existing.setTaxIdentificationNumber(dto.getTaxIdentificationNumber());
        existing.setLicenseNumber(dto.getLicenseNumber());
        existing.setWebsiteUrl(dto.getWebsiteUrl());
        existing.setEmail(dto.getEmail());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setSupportEmail(dto.getSupportEmail());
        existing.setSupportPhone(dto.getSupportPhone());
        existing.setAddressLine1(dto.getAddressLine1());
        existing.setAddressLine2(dto.getAddressLine2());
        existing.setCity(dto.getCity());
        existing.setState(dto.getState());
        existing.setCountry(dto.getCountry());
        existing.setPostalCode(dto.getPostalCode());
        existing.setLogoUrl(dto.getLogoUrl());
        existing.setFaviconUrl(dto.getFaviconUrl());
        existing.setPrimaryColor(dto.getPrimaryColor());
        existing.setSecondaryColor(dto.getSecondaryColor());
        existing.setCurrencyCode(dto.getCurrencyCode());
        existing.setTimezone(dto.getTimezone());
        existing.setBusinessType(dto.getBusinessType());
        if (dto.getCompanyStatus() != null) {
            existing.setCompanyStatus(dto.getCompanyStatus());
        } else if (existing.getCompanyStatus() == null) {
            existing.setCompanyStatus(CompanyStatus.ACTIVE);
        }
        existing.setEstablishedDate(dto.getEstablishedDate());
        existing.setRemarks(dto.getRemarks());
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        } else if (existing.getStatus() == null) {
            existing.setStatus(Status.ACTIVE);
        }

        return mapper.toDto(companyProfileRepository.save(existing));
    }

    @Override
    public CompanyProfileDto getCompanyProfileById(UUID id) {
        return companyProfileRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new GlobalException("CMP-001"));
    }

    @Override
    public List<CompanyProfileDto> getAllCompanyProfiles() {
        return companyProfileRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCompanyProfile(UUID id) {
        CompanyProfileEntity entity = companyProfileRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CMP-001"));
        entity.setStatus(Status.DELETED);
        companyProfileRepository.save(entity);
    }
}
