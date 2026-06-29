package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.MunicipalityDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface MunicipalityService {
    MunicipalityDto createMunicipality(MunicipalityDto dto);
    MunicipalityDto updateMunicipality(UUID id, MunicipalityDto dto);
    MunicipalityDto getMunicipalityById(UUID id);
    List<MunicipalityDto> getAllMunicipalities();
    List<MunicipalityDto> getMunicipalitiesByDistrict(UUID districtId);
    void deleteMunicipality(UUID id);
}
