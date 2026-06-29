package com.project.smartinsurance.customerService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.customerService.dto.IdentityTypeConfigDto;
import com.project.smartinsurance.customerService.model.IdentityTypeConfig;
import com.project.smartinsurance.customerService.repository.IdentityTypeConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/master/identity-types")
@RequiredArgsConstructor
public class IdentityTypeConfigController {

    private final IdentityTypeConfigRepository repository;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping
    public ResponseEntity<ApiResponse<List<IdentityTypeConfigDto>>> getAll() {
        List<IdentityTypeConfigDto> dtos = repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", dtos));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<IdentityTypeConfigDto>> create(@RequestBody IdentityTypeConfigDto dto) {
        IdentityTypeConfig config = new IdentityTypeConfig();
        config.setCode(dto.getCode());
        config.setName(dto.getName());
        config.setRequiresFrontImage(dto.isRequiresFrontImage());
        config.setRequiresBackImage(dto.isRequiresBackImage());
        config.setRequiresExpiryDate(dto.isRequiresExpiryDate());
        config.setRequiresIssueDate(dto.isRequiresIssueDate());
        repository.save(config);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", toDto(config)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IdentityTypeConfigDto>> update(@PathVariable Long id, @RequestBody IdentityTypeConfigDto dto) {
        IdentityTypeConfig config = repository.findById(id).orElseThrow(() -> new GlobalException("ITC-001"));
        config.setCode(dto.getCode());
        config.setName(dto.getName());
        config.setRequiresFrontImage(dto.isRequiresFrontImage());
        config.setRequiresBackImage(dto.isRequiresBackImage());
        config.setRequiresExpiryDate(dto.isRequiresExpiryDate());
        config.setRequiresIssueDate(dto.isRequiresIssueDate());
        repository.save(config);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", toDto(config)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        IdentityTypeConfig config = repository.findById(id).orElseThrow(() -> new GlobalException("ITC-001"));
        repository.delete(config);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", "Deleted"));
    }

    private IdentityTypeConfigDto toDto(IdentityTypeConfig config) {
        return IdentityTypeConfigDto.builder()
                .id(config.getId())
                .code(config.getCode())
                .name(config.getName())
                .requiresFrontImage(config.isRequiresFrontImage())
                .requiresBackImage(config.isRequiresBackImage())
                .requiresExpiryDate(config.isRequiresExpiryDate())
                .requiresIssueDate(config.isRequiresIssueDate())
                .build();
    }
}
