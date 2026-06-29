package com.project.smartinsurance.commonService.repository;

import com.project.smartinsurance.commonService.model.UserSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    Page<UserSession> findByActiveTrue(Pageable pageable);
    Page<UserSession> findByUsername(String username, Pageable pageable);
    List<UserSession> findByUsernameAndActiveTrue(String username);
    Optional<UserSession> findByTokenAndActiveTrue(String token);
}
