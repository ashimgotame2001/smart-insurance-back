package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.CompanyProfileDto;

import java.util.List;
import java.util.UUID;

public interface CompanyProfileService {
    CompanyProfileDto createCompanyProfile(CompanyProfileDto dto);
    CompanyProfileDto updateCompanyProfile(UUID id, CompanyProfileDto dto);
    CompanyProfileDto getCompanyProfileById(UUID id);
    List<CompanyProfileDto> getAllCompanyProfiles();
    void deleteCompanyProfile(UUID id);
}
