package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, UUID> {
    boolean existsByCode(String code);
    List<Municipality> findByDistrictId(UUID districtId);
}
