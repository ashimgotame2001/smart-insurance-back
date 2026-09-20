package com.project.smartinsurance.productService.repository;

import com.project.smartinsurance.productService.model.Product;
import com.project.smartinsurance.productService.model.enums.LineOfBusiness;
import com.project.smartinsurance.productService.model.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByCode(String code);
    boolean existsByCode(String code);
    Page<Product> findByCategory(String category, Pageable pageable);
    Page<Product> findByLineOfBusiness(LineOfBusiness lineOfBusiness, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByProductStatus(ProductStatus productStatus, Pageable pageable);
}
