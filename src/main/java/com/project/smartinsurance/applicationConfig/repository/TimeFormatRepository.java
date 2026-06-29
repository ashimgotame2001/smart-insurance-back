package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.TimeFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeFormatRepository extends JpaRepository<TimeFormat, UUID> {
    boolean existsByFormat(String format);
}
