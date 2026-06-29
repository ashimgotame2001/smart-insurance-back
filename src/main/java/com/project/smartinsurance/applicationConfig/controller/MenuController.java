package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.MenuDto;
import com.project.smartinsurance.applicationConfig.service.MenuService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<MenuDto>>> getMenuTree() {
        List<MenuDto> menuTree = menuService.getMenuTreeForCurrentUser();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", menuTree));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<MenuDto>> createMenu(@RequestBody MenuDto dto) {
        MenuDto created = menuService.createMenu(dto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", created));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuDto>> updateMenu(@PathVariable UUID id, @RequestBody MenuDto dto) {
        MenuDto updated = menuService.updateMenu(id, dto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updated));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuDto>> getMenuById(@PathVariable UUID id) {
        MenuDto menu = menuService.getMenuById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", menu));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuDto>>> getAllMenus() {
        List<MenuDto> menus = menuService.getAllMenus();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", menus));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable UUID id) {
        menuService.deleteMenu(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping("/load-json")
    public ResponseEntity<ApiResponse<Void>> loadMenuFromJson() {
        menuService.loadMenuFromJson();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", null));
    }
}
