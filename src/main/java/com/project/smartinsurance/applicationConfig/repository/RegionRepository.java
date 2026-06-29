package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegionRepository extends JpaRepository<Region, UUID> {
    boolean existsByCode(String code);
    List<Region> findByProvinceId(UUID provinceId);
    List<Region> findByCountryId(UUID countryId);
}
