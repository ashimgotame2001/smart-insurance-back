package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.MenuDto;
import java.util.List;
import java.util.UUID;

public interface MenuService {
    void loadMenuFromJson();
    List<MenuDto> getMenuTreeForCurrentUser();
    MenuDto createMenu(MenuDto dto);
    MenuDto updateMenu(UUID id, MenuDto dto);
    MenuDto getMenuById(UUID id);
    List<MenuDto> getAllMenus();
    void deleteMenu(UUID id);
}
