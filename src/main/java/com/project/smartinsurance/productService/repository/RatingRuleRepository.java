package com.project.smartinsurance.productService.repository;

import com.project.smartinsurance.productService.model.RatingRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RatingRuleRepository extends JpaRepository<RatingRule, UUID> {
    List<RatingRule> findByProductId(UUID productId);
}
