package com.project.smartinsurance.applicationConfig.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {
    private UUID id;
    private String name;
    private String code;
    private String path;
    private String icon;
    private String description;
    private String permission;
    private Integer displayOrder;
    private UUID parentId;
    private List<MenuDto> subMenus;
}
