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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
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
    private final CacheManager cacheManager;

    @Override
    @Transactional
    @CacheEvict(value = "menuTree", allEntries = true)
    public void loadMenuFromJson() {
        try {
            InputStream inputStream = new ClassPathResource("metadata/menu.json").getInputStream();
            List<Map<String, Object>> menuData = objectMapper.readValue(inputStream, new TypeReference<>() {});
            Set<String> codesInJson = new HashSet<>();
            collectCodes(menuData, codesInJson);
            saveMenus(menuData, null);
            List<Menu> orphans = menuRepository.findAll().stream()
                    .filter(m -> !codesInJson.contains(m.getCode()))
                    .collect(Collectors.toList());
            if (!orphans.isEmpty()) {
                menuRepository.deleteAll(orphans);
                log.info("Removed {} orphaned menus", orphans.size());
            }
            log.info("Menu data loaded successfully from JSON");
        } catch (Exception e) {
            log.error("Error loading menu data from JSON: {}", e.getMessage());
        }
    }

    private void collectCodes(List<Map<String, Object>> menuData, Set<String> codes) {
        for (Map<String, Object> data : menuData) {
            String code = (String) data.get("code");
            if (code != null) codes.add(code);
            if (data.containsKey("subMenus")) {
                List<Map<String, Object>> subMenusData = (List<Map<String, Object>>) data.get("subMenus");
                if (subMenusData != null) {
                    collectCodes(subMenusData, codes);
                }
            }
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

        List<MenuDto> fullTree = getFullMenuTree();
        return fullTree.stream()
                .map(menu -> filterMenuDto(menu, authorities))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<MenuDto> getFullMenuTree() {
        Cache cache = cacheManager.getCache("menuTree");
        if (cache != null) {
            List<MenuDto> cached = cache.get("fullTree", List.class);
            if (cached != null) {
                return cached;
            }
        }
        List<Menu> rootMenus = menuRepository.findByParentIsNullAndStatus(Status.ACTIVE);
        log.info("Loaded {} root menus from DB", rootMenus.size());
        List<MenuDto> tree = rootMenus.stream()
                .map(this::buildFullMenuDto)
                .collect(Collectors.toList());
        if (cache != null) {
            cache.put("fullTree", tree);
        }
        return tree;
    }

    private MenuDto buildFullMenuDto(Menu menu) {
        List<MenuDto> subMenuDtos = new ArrayList<>();
        if (menu.getSubMenus() != null) {
            for (Menu subMenu : menu.getSubMenus()) {
                if (subMenu.getStatus() != null && subMenu.getStatus() == Status.ACTIVE) {
                    subMenuDtos.add(buildFullMenuDto(subMenu));
                }
            }
        }
        return toDto(menu, subMenuDtos);
    }

    private MenuDto filterMenuDto(MenuDto source, Set<String> authorities) {
        if (source.getPermission() != null && !source.getPermission().isEmpty()
                && !authorities.contains(source.getPermission())) {
            return null;
        }
        List<MenuDto> filteredChildren = null;
        if (source.getSubMenus() != null) {
            filteredChildren = new ArrayList<>();
            for (MenuDto child : source.getSubMenus()) {
                MenuDto filtered = filterMenuDto(child, authorities);
                if (filtered != null) {
                    filteredChildren.add(filtered);
                }
            }
        }
        return MenuDto.builder()
                .id(source.getId())
                .name(source.getName())
                .code(source.getCode())
                .path(source.getPath())
                .icon(source.getIcon())
                .description(source.getDescription())
                .permission(source.getPermission())
                .displayOrder(source.getDisplayOrder())
                .subMenus(filteredChildren)
                .build();
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
    @CacheEvict(value = "menuTree", allEntries = true)
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
    @CacheEvict(value = "menuTree", allEntries = true)
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
    @CacheEvict(value = "menuTree", allEntries = true)
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
