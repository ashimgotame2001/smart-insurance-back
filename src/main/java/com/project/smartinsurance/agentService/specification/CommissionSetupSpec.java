package com.project.smartinsurance.agentService.specification;

import com.project.smartinsurance.agentService.dto.CommissionSetupFilterRequest;
import com.project.smartinsurance.agentService.model.CommissionSetup;
import com.project.smartinsurance.commonService.specification.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;

public final class CommissionSetupSpec {

    private CommissionSetupSpec() {
    }

    public static Specification<CommissionSetup> toSpecification(CommissionSetupFilterRequest filter) {
        if (filter == null) {
            return SpecificationUtils.notDeleted();
        }
        return SpecificationUtils.and(
                filter.getStatus() != null
                        ? SpecificationUtils.equal("status", filter.getStatus())
                        : SpecificationUtils.notDeleted(),
                SpecificationUtils.likeIgnoreCase("code", filter.getCode()),
                SpecificationUtils.likeIgnoreCase("name", filter.getName()),
                SpecificationUtils.likeIgnoreCase("agentCategory", filter.getAgentCategory()),
                SpecificationUtils.equal("structureType", filter.getStructureType())
        );
    }
}
