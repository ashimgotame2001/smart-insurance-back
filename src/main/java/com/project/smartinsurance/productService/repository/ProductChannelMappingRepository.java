package com.project.smartinsurance.productService.repository;

import com.project.smartinsurance.productService.model.ProductChannelMapping;
import com.project.smartinsurance.productService.model.enums.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductChannelMappingRepository extends JpaRepository<ProductChannelMapping, UUID> {
    List<ProductChannelMapping> findByProductId(UUID productId);
    Optional<ProductChannelMapping> findByProductIdAndChannel(UUID productId, Channel channel);
}
