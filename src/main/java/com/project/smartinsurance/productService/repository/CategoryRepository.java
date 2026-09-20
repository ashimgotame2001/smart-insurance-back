package com.project.smartinsurance.productService.repository;

import com.project.smartinsurance.productService.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByDeletedFalseOrderBySortOrderAsc();
    Optional<Category> findByCode(String code);
    boolean existsByCode(String code);
}