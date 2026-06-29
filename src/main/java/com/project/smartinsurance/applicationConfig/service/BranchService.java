package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.BranchDto;

import java.util.List;
import java.util.UUID;

public interface BranchService {
    BranchDto createBranch(BranchDto dto);
    BranchDto updateBranch(UUID id, BranchDto dto);
    BranchDto getBranchById(UUID id);
    List<BranchDto> getAllBranches();
    List<BranchDto> getBranchesByParent(UUID parentId);
    List<BranchDto> getRootBranches();
    void deleteBranch(UUID id);
}
