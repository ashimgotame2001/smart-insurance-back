package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, UUID> {
    boolean existsByBranchCode(String code);
    List<BranchEntity> findByParentBranchIsNull();
    List<BranchEntity> findByParentBranchId(UUID parentId);
}
