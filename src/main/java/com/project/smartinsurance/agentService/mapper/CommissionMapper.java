package com.project.smartinsurance.agentService.mapper;

import com.project.smartinsurance.agentService.dto.CommissionRuleDto;
import com.project.smartinsurance.agentService.dto.CommissionSetupDto;
import com.project.smartinsurance.agentService.model.CommissionRule;
import com.project.smartinsurance.agentService.model.CommissionSetup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommissionMapper {

    @Mapping(target = "entityStatus", source = "status")
    CommissionSetupDto toDto(CommissionSetup entity);

    CommissionSetup toEntity(CommissionSetupDto dto);

    @Mapping(target = "entityStatus", source = "status")
    @Mapping(target = "commissionSetupCode", ignore = true)
    @Mapping(target = "commissionSetupName", ignore = true)
    CommissionRuleDto toDto(CommissionRule entity);

    CommissionRule toEntity(CommissionRuleDto dto);
}
