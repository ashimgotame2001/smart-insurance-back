package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.CommissionSlabDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface CommissionSlabService {
    CommissionSlabDto createCommissionSlab(CommissionSlabDto dto);
    CommissionSlabDto updateCommissionSlab(UUID id, CommissionSlabDto dto);
    CommissionSlabDto getCommissionSlabById(UUID id);
    List<CommissionSlabDto> getAllCommissionSlabs();
    void deleteCommissionSlab(UUID id);
}
