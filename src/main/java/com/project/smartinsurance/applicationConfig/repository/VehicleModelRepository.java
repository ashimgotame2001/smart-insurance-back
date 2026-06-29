package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, UUID> {
    boolean existsByCode(String code);
    List<VehicleModel> findByBrandId(UUID brandId);
}
