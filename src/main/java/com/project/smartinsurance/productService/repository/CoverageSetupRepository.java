package com.project.smartinsurance.productService.repository;

import com.project.smartinsurance.productService.model.CoverageSetup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoverageSetupRepository extends JpaRepository<CoverageSetup, UUID> {
    List<CoverageSetup> findByProductIdOrderByCreatedAtAsc(UUID productId);
    Page<CoverageSetup> findByDeletedFalse(Pageable pageable);
    Page<CoverageSetup> findByProductIdAndDeletedFalse(UUID productId, Pageable pageable);
    Optional<CoverageSetup> findByCode(String code);
}