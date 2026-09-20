package com.project.smartinsurance.agentService.specification;

import com.project.smartinsurance.agentService.dto.CommissionRuleFilterRequest;
import com.project.smartinsurance.agentService.model.CommissionRule;
import com.project.smartinsurance.commonService.specification.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;

public final class CommissionRuleSpec {

    private CommissionRuleSpec() {
    }

    public static Specification<CommissionRule> toSpecification(CommissionRuleFilterRequest filter) {
        if (filter == null) {
            return SpecificationUtils.notDeleted();
        }
        return SpecificationUtils.and(
                filter.getStatus() != null
                        ? SpecificationUtils.equal("status", filter.getStatus())
                        : SpecificationUtils.notDeleted(),
                SpecificationUtils.likeIgnoreCase("code", filter.getCode()),
                SpecificationUtils.likeIgnoreCase("name", filter.getName()),
                SpecificationUtils.equal("commissionSetupId", filter.getCommissionSetupId()),
                SpecificationUtils.equal("productId", filter.getProductId()),
                SpecificationUtils.equal("ruleType", filter.getRuleType()),
                SpecificationUtils.likeIgnoreCase("agentCategory", filter.getAgentCategory())
        );
    }
}
