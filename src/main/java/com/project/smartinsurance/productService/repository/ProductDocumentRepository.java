package com.project.smartinsurance.productService.repository;

import com.project.smartinsurance.productService.model.ProductDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductDocumentRepository extends JpaRepository<ProductDocument, UUID> {
    List<ProductDocument> findByProductId(UUID productId);
}
