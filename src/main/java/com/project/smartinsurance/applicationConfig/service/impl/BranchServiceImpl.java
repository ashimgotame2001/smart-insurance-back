package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.applicationConfig.dto.BranchDto;
import com.project.smartinsurance.applicationConfig.mapper.CompanyProfileMapper;
import com.project.smartinsurance.applicationConfig.model.BranchEntity;
import com.project.smartinsurance.applicationConfig.model.enums.BranchStatus;
import com.project.smartinsurance.applicationConfig.repository.BranchRepository;
import com.project.smartinsurance.applicationConfig.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final CompanyProfileMapper mapper;

    private UUID getCurrentBranchId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getDetails() instanceof Map<?, ?> details) {
            Object branchIdObj = details.get("branchId");
            if (branchIdObj != null) {
                return UUID.fromString(branchIdObj.toString());
            }
        }
        return null;
    }

    @Override
    @Transactional
    public BranchDto createBranch(BranchDto dto) {
        BranchEntity entity = mapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        if (entity.getBranchStatus() == null) {
            entity.setBranchStatus(BranchStatus.ACTIVE);
        }
        return mapper.toDto(branchRepository.save(entity));
    }

    @Override
    @Transactional
    public BranchDto updateBranch(UUID id, BranchDto dto) {
        BranchEntity existing = branchRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BRN-001"));

        existing.setBranchName(dto.getBranchName());
        existing.setBranchCode(dto.getBranchCode());
        existing.setBranchType(dto.getBranchType());
        existing.setBranchStatus(dto.getBranchStatus());
        existing.setEmail(dto.getEmail());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setAddressLine1(dto.getAddressLine1());
        existing.setAddressLine2(dto.getAddressLine2());
        existing.setCity(dto.getCity());
        existing.setState(dto.getState());
        existing.setCountry(dto.getCountry());
        existing.setPostalCode(dto.getPostalCode());
        existing.setHeadOffice(dto.isHeadOffice());
        existing.setStatus(dto.getStatus());

        if (dto.getParentBranchId() != null) {
            BranchEntity parent = branchRepository.findById(dto.getParentBranchId())
                    .orElseThrow(() -> new GlobalException("BRN-001"));
            existing.setParentBranch(parent);
        } else {
            existing.setParentBranch(null);
        }

        return mapper.toDto(branchRepository.save(existing));
    }

    @Override
    public BranchDto getBranchById(UUID id) {
        UUID userBranchId = getCurrentBranchId();
        if (userBranchId != null && !userBranchId.equals(id)) {
            throw new GlobalException("AUTH-005");
        }
        return branchRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new GlobalException("BRN-001"));
    }

    @Override
    public List<BranchDto> getAllBranches() {
        UUID userBranchId = getCurrentBranchId();
        List<BranchEntity> branches;
        if (userBranchId != null) {
            branches = branchRepository.findAllById(List.of(userBranchId));
        } else {
            branches = branchRepository.findAll();
        }
        return branches.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BranchDto> getBranchesByParent(UUID parentId) {
        return branchRepository.findByParentBranchId(parentId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BranchDto> getRootBranches() {
        UUID userBranchId = getCurrentBranchId();
        if (userBranchId != null) {
            return branchRepository.findAllById(List.of(userBranchId)).stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
        return branchRepository.findByParentBranchIsNull().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBranch(UUID id) {
        UUID userBranchId = getCurrentBranchId();
        if (userBranchId != null && !userBranchId.equals(id)) {
            throw new GlobalException("AUTH-005");
        }
        BranchEntity entity = branchRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BRN-001"));
        entity.setStatus(Status.DELETED);
        entity.setBranchStatus(BranchStatus.DELETED);
        branchRepository.save(entity);
    }
}
