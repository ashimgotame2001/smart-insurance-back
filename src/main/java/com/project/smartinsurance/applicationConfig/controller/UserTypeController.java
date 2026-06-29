package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.UserTypeDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.UserType;
import com.project.smartinsurance.applicationConfig.repository.UserTypeRepository;
import com.project.smartinsurance.applicationConfig.service.UserTypeService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/master/user-types")
@RequiredArgsConstructor
public class UserTypeController {

    private final UserTypeService userTypeService;
    private final UserTypeRepository userTypeRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserTypeDto>> createUserType(@RequestBody UserTypeDto userTypeDto) {
        UserTypeDto createdUserType = userTypeService.createUserType(userTypeDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", createdUserType));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserTypeDto>> updateUserType(@PathVariable UUID id, @RequestBody UserTypeDto userTypeDto) {
        UserTypeDto updatedUserType = userTypeService.updateUserType(id, userTypeDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updatedUserType));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserTypeDto>> getUserTypeById(@PathVariable UUID id) {
        UserTypeDto userType = userTypeService.getUserTypeById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", userType));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<UserTypeDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = userTypeRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<UserTypeDto> paged = PagedData.<UserTypeDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((UserType) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserType(@PathVariable UUID id) {
        userTypeService.deleteUserType(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
