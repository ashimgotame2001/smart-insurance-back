package com.project.smartinsurance.productService.repository;

import com.project.smartinsurance.productService.model.ProductVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductVersionRepository extends JpaRepository<ProductVersion, UUID> {
    List<ProductVersion> findByProductIdOrderByVersionNumberDesc(UUID productId);
}
