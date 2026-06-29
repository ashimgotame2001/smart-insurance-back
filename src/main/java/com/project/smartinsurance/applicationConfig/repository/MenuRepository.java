package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.Menu;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID> {
    List<Menu> findByParentIsNullAndStatusOrderByDisplayOrderAsc(Status status);
    Optional<Menu> findByCode(String code);
    boolean existsByCode(String code);
}
