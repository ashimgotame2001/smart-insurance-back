package com.project.smartinsurance.identityService.repository;

import com.project.smartinsurance.identityService.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByUsernameNot(String username);
    Page<User> findByUsernameNot(String username, Pageable pageable);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByUserGroupId(UUID groupId);
}
