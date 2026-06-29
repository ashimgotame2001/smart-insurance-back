package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "master_time_formats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeFormat extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String format;

    @Column(nullable = false)
    private String description;
}
