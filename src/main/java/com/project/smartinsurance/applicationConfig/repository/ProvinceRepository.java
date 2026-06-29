package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, UUID> {
    boolean existsByCode(String code);
    List<Province> findByCountryId(UUID countryId);
}
