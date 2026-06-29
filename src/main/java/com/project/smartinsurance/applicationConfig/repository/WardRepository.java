package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WardRepository extends JpaRepository<Ward, UUID> {
    boolean existsByCode(String code);
    List<Ward> findByMunicipalityId(UUID municipalityId);
}
