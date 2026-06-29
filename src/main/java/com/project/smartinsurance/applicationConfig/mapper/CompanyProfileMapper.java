package com.project.smartinsurance.applicationConfig.mapper;

import com.project.smartinsurance.applicationConfig.dto.BranchDto;
import com.project.smartinsurance.applicationConfig.dto.CompanyProfileDto;
import com.project.smartinsurance.applicationConfig.model.BranchEntity;
import com.project.smartinsurance.applicationConfig.model.CompanyProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CompanyProfileMapper {

    CompanyProfileDto toDto(CompanyProfileEntity entity);
    CompanyProfileEntity toEntity(CompanyProfileDto dto);

    @Mapping(target = "parentBranchId", source = "parentBranch.id")
    BranchDto toDto(BranchEntity entity);

    @Mapping(target = "parentBranch", source = "parentBranchId")
    BranchEntity toEntity(BranchDto dto);

    default BranchEntity mapBranchIdToBranch(UUID branchId) {
        if (branchId == null) {
            return null;
        }
        BranchEntity branch = new BranchEntity();
        branch.setId(branchId);
        return branch;
    }
}
