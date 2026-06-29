package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.ReinsuranceCompanyDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface ReinsuranceCompanyService {
    ReinsuranceCompanyDto createReinsuranceCompany(ReinsuranceCompanyDto dto);
    ReinsuranceCompanyDto updateReinsuranceCompany(UUID id, ReinsuranceCompanyDto dto);
    ReinsuranceCompanyDto getReinsuranceCompanyById(UUID id);
    List<ReinsuranceCompanyDto> getAllReinsuranceCompanies();
    void deleteReinsuranceCompany(UUID id);
}
