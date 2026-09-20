package com.project.smartinsurance.productService.mapper;

import com.project.smartinsurance.productService.dto.*;
import com.project.smartinsurance.productService.model.*;
import com.project.smartinsurance.productService.model.enums.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductMapper {

    @Mapping(target = "lineOfBusiness", expression = "java(product.getLineOfBusiness() != null ? product.getLineOfBusiness().name() : null)")
    @Mapping(target = "productStatus", expression = "java(product.getProductStatus() != null ? product.getProductStatus().name() : null)")
    @Mapping(target = "status", expression = "java(product.getStatus() != null ? product.getStatus().name() : null)")
    public abstract ProductDto toDto(Product product);

    @Mapping(target = "premiumType", expression = "java(plan.getPremiumType() != null ? plan.getPremiumType().name() : null)")
    @Mapping(target = "status", expression = "java(plan.getStatus() != null ? plan.getStatus().name() : null)")
    public abstract ProductPlanDto toPlanDto(ProductPlan plan);

    @Mapping(target = "coverageType", expression = "java(coverage.getCoverageType() != null ? coverage.getCoverageType().name() : null)")
    @Mapping(target = "status", expression = "java(coverage.getStatus() != null ? coverage.getStatus().name() : null)")
    public abstract ProductCoverageDto toCoverageDto(ProductCoverage coverage);

    @Mapping(target = "productId", source = "eligibilityRule.product.id")
    @Mapping(target = "status", expression = "java(eligibilityRule.getStatus() != null ? eligibilityRule.getStatus().name() : null)")
    public abstract EligibilityRuleDto toEligibilityRuleDto(EligibilityRule eligibilityRule);

    @Mapping(target = "productId", source = "ratingRule.product.id")
    @Mapping(target = "status", expression = "java(ratingRule.getStatus() != null ? ratingRule.getStatus().name() : null)")
    public abstract RatingRuleDto toRatingRuleDto(RatingRule ratingRule);

    @Mapping(target = "productId", source = "productVersion.product.id")
    @Mapping(target = "status", expression = "java(productVersion.getStatus() != null ? productVersion.getStatus().name() : null)")
    public abstract ProductVersionDto toProductVersionDto(ProductVersion productVersion);

    @Mapping(target = "productId", source = "mapping.product.id")
    @Mapping(target = "channel", expression = "java(mapping.getChannel() != null ? mapping.getChannel().name() : null)")
    @Mapping(target = "status", expression = "java(mapping.getStatus() != null ? mapping.getStatus().name() : null)")
    public abstract ProductChannelMappingDto toChannelMappingDto(ProductChannelMapping mapping);

    @Mapping(target = "productId", source = "document.product.id")
    @Mapping(target = "status", expression = "java(document.getStatus() != null ? document.getStatus().name() : null)")
    public abstract ProductDocumentDto toDocumentDto(ProductDocument document);

    public void updateEntity(Product product, ProductUpdateRequest request) {
        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        // category is resolved/validated in ProductServiceImpl
        if (request.getLineOfBusiness() != null) product.setLineOfBusiness(LineOfBusiness.valueOf(request.getLineOfBusiness()));
        if (request.getEffectiveFrom() != null) product.setEffectiveFrom(request.getEffectiveFrom());
        if (request.getEffectiveTo() != null) product.setEffectiveTo(request.getEffectiveTo());
        if (request.getMinSumAssured() != null) product.setMinSumAssured(request.getMinSumAssured());
        if (request.getMaxSumAssured() != null) product.setMaxSumAssured(request.getMaxSumAssured());
        if (request.getMinTerm() != null) product.setMinTerm(request.getMinTerm());
        if (request.getMaxTerm() != null) product.setMaxTerm(request.getMaxTerm());
        if (request.getWaitingPeriod() != null) product.setWaitingPeriod(request.getWaitingPeriod());
        if (request.getCoolingOffPeriod() != null) product.setCoolingOffPeriod(request.getCoolingOffPeriod());
        if (request.getRenewalPeriod() != null) product.setRenewalPeriod(request.getRenewalPeriod());
        if (request.getLatePaymentGracePeriod() != null) product.setLatePaymentGracePeriod(request.getLatePaymentGracePeriod());
        if (request.getPolicyFee() != null) product.setPolicyFee(request.getPolicyFee());
        if (request.getCommissionRate() != null) product.setCommissionRate(request.getCommissionRate());
        if (request.getMaxCommissionRate() != null) product.setMaxCommissionRate(request.getMaxCommissionRate());
        if (request.getCommissionStructure() != null) product.setCommissionStructure(request.getCommissionStructure());
        if (request.getGroupProduct() != null) product.setGroupProduct(request.getGroupProduct());
        if (request.getLinkedProductIds() != null) product.setLinkedProductIds(request.getLinkedProductIds());
        if (request.getAllowPartialWithdrawal() != null) product.setAllowPartialWithdrawal(request.getAllowPartialWithdrawal());
        if (request.getSurrenderChargeSchedule() != null) product.setSurrenderChargeSchedule(request.getSurrenderChargeSchedule());
        if (request.getTaxDeductible() != null) product.setTaxDeductible(request.getTaxDeductible());
        if (request.getTaxRate() != null) product.setTaxRate(request.getTaxRate());
        if (request.getRegulatoryApprovalRequired() != null) product.setRegulatoryApprovalRequired(request.getRegulatoryApprovalRequired());
        if (request.getRegulatoryApprovalNumber() != null) product.setRegulatoryApprovalNumber(request.getRegulatoryApprovalNumber());
        if (request.getRegulatorReportingRequired() != null) product.setRegulatorReportingRequired(request.getRegulatorReportingRequired());
        if (request.getProductFeatures() != null) product.setProductFeatures(request.getProductFeatures());
        if (request.getTermsAndConditions() != null) product.setTermsAndConditions(request.getTermsAndConditions());
        if (request.getPolicyWording() != null) product.setPolicyWording(request.getPolicyWording());
    }

    public void updatePlanEntity(ProductPlan plan, ProductPlanUpdateRequest request) {
        if (request.getName() != null) plan.setName(request.getName());
        if (request.getDescription() != null) plan.setDescription(request.getDescription());
        if (request.getMinEntryAge() != null) plan.setMinEntryAge(request.getMinEntryAge());
        if (request.getMaxEntryAge() != null) plan.setMaxEntryAge(request.getMaxEntryAge());
        if (request.getMinSumAssured() != null) plan.setMinSumAssured(request.getMinSumAssured());
        if (request.getMaxSumAssured() != null) plan.setMaxSumAssured(request.getMaxSumAssured());
        if (request.getMinTerm() != null) plan.setMinTerm(request.getMinTerm());
        if (request.getMaxTerm() != null) plan.setMaxTerm(request.getMaxTerm());
        if (request.getPremiumType() != null) plan.setPremiumType(PremiumType.valueOf(request.getPremiumType()));
        if (request.getPremiumFrequency() != null) plan.setPremiumFrequency(request.getPremiumFrequency());
        if (request.getPremiumPayingTerm() != null) plan.setPremiumPayingTerm(request.getPremiumPayingTerm());
    }

    public void updateCoverageEntity(ProductCoverage coverage, ProductCoverageUpdateRequest request) {
        if (request.getName() != null) coverage.setName(request.getName());
        if (request.getDescription() != null) coverage.setDescription(request.getDescription());
        if (request.getCoverageType() != null) coverage.setCoverageType(CoverageType.valueOf(request.getCoverageType()));
        if (request.getSumAssuredAmount() != null) coverage.setSumAssuredAmount(request.getSumAssuredAmount());
        if (request.getPremiumRate() != null) coverage.setPremiumRate(request.getPremiumRate());
        if (request.getPremiumAmount() != null) coverage.setPremiumAmount(request.getPremiumAmount());
        if (request.getBenefitAmount() != null) coverage.setBenefitAmount(request.getBenefitAmount());
        if (request.getWaitingPeriod() != null) coverage.setWaitingPeriod(request.getWaitingPeriod());
        if (request.getDeductible() != null) coverage.setDeductible(request.getDeductible());
        if (request.getCoinsurance() != null) coverage.setCoinsurance(request.getCoinsurance());
        if (request.getBenefitPeriod() != null) coverage.setBenefitPeriod(request.getBenefitPeriod());
        if (request.getMaxBenefit() != null) coverage.setMaxBenefit(request.getMaxBenefit());
        if (request.getCoverageTerms() != null) coverage.setCoverageTerms(request.getCoverageTerms());
    }
}
