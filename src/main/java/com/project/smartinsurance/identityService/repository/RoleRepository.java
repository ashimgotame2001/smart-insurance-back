package com.project.smartinsurance.identityService.repository;

import com.project.smartinsurance.identityService.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByCode(String code);
    List<Role> findByCodeNot(String code);
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
