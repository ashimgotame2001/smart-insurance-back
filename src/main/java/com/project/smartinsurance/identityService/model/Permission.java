package com.project.smartinsurance.identityService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    @Column(name = "ui_access_path")
    private String uiAccessPath;

    @Column(name = "is_checker_maker_enabled")
    private boolean checkerMakerEnabled;
}
