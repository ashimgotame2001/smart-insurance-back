package com.project.smartinsurance.productService.repository;

import com.project.smartinsurance.productService.model.Exclusion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExclusionRepository extends JpaRepository<Exclusion, UUID> {
    List<Exclusion> findByProductIdOrderByCreatedAtAsc(UUID productId);
    Page<Exclusion> findByDeletedFalse(Pageable pageable);
    Page<Exclusion> findByProductIdAndDeletedFalse(UUID productId, Pageable pageable);
    Optional<Exclusion> findByCode(String code);
}