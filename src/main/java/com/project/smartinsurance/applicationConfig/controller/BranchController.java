package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.applicationConfig.dto.BranchDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.repository.BranchRepository;
import com.project.smartinsurance.applicationConfig.service.BranchService;
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
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;
    private final BranchRepository branchRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_BRANCHES_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BranchDto>> createBranch(@RequestBody BranchDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", branchService.createBranch(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_BRANCHES_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchDto>> updateBranch(@PathVariable UUID id, @RequestBody BranchDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", branchService.updateBranch(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_BRANCHES_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchDto>> getBranchById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", branchService.getBranchById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_BRANCHES_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<BranchDto>>> getAllBranches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = branchRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<BranchDto> paged = PagedData.<BranchDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((com.project.smartinsurance.applicationConfig.model.BranchEntity) e)).collect(Collectors.toList()))
                .page(result.getNumber()).size(result.getSize())
                .totalElements(result.getTotalElements()).totalPages(result.getTotalPages()).build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<ApiResponse<List<BranchDto>>> getBranchesByParent(@PathVariable UUID parentId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", branchService.getBranchesByParent(parentId)));
    }

    @GetMapping("/roots")
    public ResponseEntity<ApiResponse<List<BranchDto>>> getRootBranches() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", branchService.getRootBranches()));
    }

    @PreAuthorize("hasAuthority('PERM_BRANCHES_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBranch(@PathVariable UUID id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
