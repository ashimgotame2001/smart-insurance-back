package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.LookupValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LookupValueRepository extends JpaRepository<LookupValue, UUID> {
    boolean existsByCategoryAndCode(String category, String code);
    List<LookupValue> findByCategory(String category);
    Optional<LookupValue> findByCategoryAndCode(String category, String code);
}
