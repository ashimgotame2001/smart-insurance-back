package com.project.smartinsurance.productService.repository;

import com.project.smartinsurance.productService.model.ProductCoverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductCoverageRepository extends JpaRepository<ProductCoverage, UUID> {
    List<ProductCoverage> findByPlanIdOrderByCreatedAtAsc(UUID planId);
}
