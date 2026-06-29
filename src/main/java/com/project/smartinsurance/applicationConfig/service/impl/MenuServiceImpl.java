package com.project.smartinsurance.applicationConfig.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.smartinsurance.applicationConfig.dto.MenuDto;
import com.project.smartinsurance.applicationConfig.model.Menu;
import com.project.smartinsurance.applicationConfig.repository.MenuRepository;
import com.project.smartinsurance.applicationConfig.service.MenuService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void loadMenuFromJson() {
        try {
            InputStream inputStream = new ClassPathResource("metadata/menu.json").getInputStream();
            List<Map<String, Object>> menuData = objectMapper.readValue(inputStream, new TypeReference<>() {});
            saveMenus(menuData, null);
            log.info("Menu data loaded successfully from JSON");
        } catch (Exception e) {
            log.error("Error loading menu data from JSON: {}", e.getMessage());
        }
    }

    private void saveMenus(List<Map<String, Object>> menuData, Menu parent) {
        int order = 1;
        for (Map<String, Object> data : menuData) {
            String code = (String) data.get("code");
            Menu menu = menuRepository.findByCode(code).orElse(new Menu());
            
            menu.setName((String) data.get("name"));
            menu.setCode(code);
            menu.setPath((String) data.get("path"));
            menu.setIcon((String) data.get("icon"));
            menu.setDescription((String) data.get("description"));
            menu.setPermission((String) data.get("permission"));
            menu.setDisplayOrder(order++);
            menu.setParent(parent);
            menu.setStatus(Status.ACTIVE);

            menu = menuRepository.save(menu);
            log.info("Saved/Updated menu: {} ({})", menu.getName(), menu.getCode());

            if (data.containsKey("subMenus")) {
                List<Map<String, Object>> subMenusData = (List<Map<String, Object>>) data.get("subMenus");
                if (subMenusData != null && !subMenusData.isEmpty()) {
                    saveMenus(subMenusData, menu);
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDto> getMenuTreeForCurrentUser() {
        Set<String> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        List<Menu> rootMenus = menuRepository.findByParentIsNullAndStatusOrderByDisplayOrderAsc(Status.ACTIVE);
        log.info("Found {} root menus for current user", rootMenus.size());
        return rootMenus.stream()
                .map(menu -> convertToDto(menu, authorities))
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    private MenuDto convertToDto(Menu menu, Set<String> authorities) {
        if (menu.getPermission() != null && !menu.getPermission().isEmpty()
                && !authorities.contains(menu.getPermission())) {
            return null;
        }

        List<MenuDto> subMenuDtos = new ArrayList<>();
        if (menu.getSubMenus() != null) {
            for (Menu subMenu : menu.getSubMenus()) {
                if (subMenu.getStatus() != null && subMenu.getStatus() == Status.ACTIVE) {
                    MenuDto subDto = convertToDto(subMenu, authorities);
                    if (subDto != null) {
                        subMenuDtos.add(subDto);
                    }
                }
            }
        }

        return toDto(menu, subMenuDtos);
    }

    private MenuDto toDto(Menu menu, List<MenuDto> subMenuDtos) {
        return MenuDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .code(menu.getCode())
                .path(menu.getPath())
                .icon(menu.getIcon())
                .description(menu.getDescription())
                .permission(menu.getPermission())
                .displayOrder(menu.getDisplayOrder())
                .subMenus(subMenuDtos)
                .build();
    }

    @Override
    @Transactional
    public MenuDto createMenu(MenuDto dto) {
        if (menuRepository.existsByCode(dto.getCode())) {
            throw new GlobalException("MNU-002", dto.getCode());
        }
        Menu menu = new Menu();
        updateMenuFields(menu, dto);
        menu.setStatus(Status.ACTIVE);
        return toDto(menuRepository.save(menu), null);
    }

    @Override
    @Transactional
    public MenuDto updateMenu(UUID id, MenuDto dto) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new GlobalException("MNU-001", id));
        updateMenuFields(menu, dto);
        return toDto(menuRepository.save(menu), null);
    }

    @Override
    public MenuDto getMenuById(UUID id) {
        return menuRepository.findById(id)
                .map(menu -> toDto(menu, null))
                .orElseThrow(() -> new GlobalException("MNU-001", id));
    }

    @Override
    public List<MenuDto> getAllMenus() {
        return menuRepository.findAll().stream()
                .map(menu -> toDto(menu, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMenu(UUID id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new GlobalException("MNU-001", id));
        menu.setStatus(Status.DELETED);
        menuRepository.save(menu);
    }

    private void updateMenuFields(Menu menu, MenuDto dto) {
        menu.setName(dto.getName());
        menu.setCode(dto.getCode());
        menu.setPath(dto.getPath());
        menu.setIcon(dto.getIcon());
        menu.setDescription(dto.getDescription());
        menu.setPermission(dto.getPermission());
        menu.setDisplayOrder(dto.getDisplayOrder());
        if (dto.getParentId() != null) {
            Menu parent = menuRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new GlobalException("MNU-001", dto.getParentId()));
            menu.setParent(parent);
        }
    }
}
