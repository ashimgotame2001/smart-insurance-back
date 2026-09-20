package com.project.smartinsurance.identityService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Sidebar-shaped menu node with permissions for role assignment.
 * Mirrors menu tree: Menu → Submenu → nested children.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuPermissionDto {
    private UUID id;
    private String name;
    private String code;
    private String path;
    private String icon;
    private String description;
    private Integer displayOrder;

    @Builder.Default
    private List<PermissionDto> permissions = new ArrayList<>();

    @Builder.Default
    private List<MenuPermissionDto> subMenus = new ArrayList<>();
}
