package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.VehicleModelDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.VehicleModel;
import com.project.smartinsurance.applicationConfig.repository.VehicleModelRepository;
import com.project.smartinsurance.applicationConfig.service.VehicleModelService;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/master/vehicle-models")
@RequiredArgsConstructor
public class VehicleModelController {

    private final VehicleModelService vehicleModelService;
    private final VehicleModelRepository vehicleModelRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<VehicleModelDto>> create(@RequestBody VehicleModelDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", vehicleModelService.createVehicleModel(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleModelDto>> update(@PathVariable UUID id, @RequestBody VehicleModelDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", vehicleModelService.updateVehicleModel(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleModelDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", vehicleModelService.getVehicleModelById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<VehicleModelDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = vehicleModelRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<VehicleModelDto> paged = PagedData.<VehicleModelDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((VehicleModel) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @GetMapping("/by-brand/{brandId}")
    public ResponseEntity<ApiResponse<List<VehicleModelDto>>> getByBrand(@PathVariable UUID brandId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", vehicleModelService.getVehicleModelsByBrand(brandId)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        vehicleModelService.deleteVehicleModel(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
