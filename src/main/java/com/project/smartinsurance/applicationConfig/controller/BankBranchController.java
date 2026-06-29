package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.BankBranchDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.BankBranch;
import com.project.smartinsurance.applicationConfig.repository.BankBranchRepository;
import com.project.smartinsurance.applicationConfig.service.BankBranchService;
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
@RequestMapping("/api/v1/master/bank-branches")
@RequiredArgsConstructor
public class BankBranchController {

    private final BankBranchService bankBranchService;
    private final BankBranchRepository bankBranchRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BankBranchDto>> create(@RequestBody BankBranchDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", bankBranchService.createBankBranch(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BankBranchDto>> update(@PathVariable UUID id, @RequestBody BankBranchDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", bankBranchService.updateBankBranch(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BankBranchDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", bankBranchService.getBankBranchById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<BankBranchDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = bankBranchRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<BankBranchDto> paged = PagedData.<BankBranchDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((BankBranch) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @GetMapping("/by-bank/{bankId}")
    public ResponseEntity<ApiResponse<List<BankBranchDto>>> getByBank(@PathVariable UUID bankId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", bankBranchService.getBankBranchesByBank(bankId)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        bankBranchService.deleteBankBranch(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
