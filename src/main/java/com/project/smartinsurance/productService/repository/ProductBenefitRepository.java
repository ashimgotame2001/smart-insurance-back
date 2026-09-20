package com.project.smartinsurance.productService.repository;

import com.project.smartinsurance.productService.model.ProductBenefit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductBenefitRepository extends JpaRepository<ProductBenefit, UUID> {
    List<ProductBenefit> findByProductIdOrderByCreatedAtAsc(UUID productId);
    Page<ProductBenefit> findByDeletedFalse(Pageable pageable);
    Page<ProductBenefit> findByProductIdAndDeletedFalse(UUID productId, Pageable pageable);
    Optional<ProductBenefit> findByCode(String code);
}