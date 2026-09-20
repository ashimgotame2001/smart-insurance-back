package com.project.smartinsurance.productService.repository;

import com.project.smartinsurance.productService.model.ProductPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductPlanRepository extends JpaRepository<ProductPlan, UUID> {
    List<ProductPlan> findByProductIdOrderByCreatedAtAsc(UUID productId);
    Optional<ProductPlan> findByCode(String code);
    boolean existsByCode(String code);
}
