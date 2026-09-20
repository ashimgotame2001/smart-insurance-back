# Module: `productService`

| Field | Value |
|-------|-------|
| Status | `implemented` |
| Java package | `com.project.smartinsurance.productService` |
| API prefix | `/api/v1/products` |
| Error-code prefixes | `PRD-*`, `BNF-*`, coverage/exclusion codes |

## Purpose

Insurance product catalog: products, plans, coverages, benefits, exclusions, categories, documents, versions, eligibility rules, rating (pricing) rules, channel mappings, and lifecycle (submit → approve → activate / suspend / retire).

## Owned entities

- Product, ProductPlan, ProductCoverage, CoverageSetup
- ProductBenefit, Exclusion, ProductCategory
- ProductDocument, ProductVersion
- EligibilityRule, RatingRule, ProductChannelMapping

## Dependencies

| Direction | Module | Access style | Why |
|-----------|--------|--------------|-----|
| Outbound | `commonService` | shared kernel | ApiResponse, exceptions, paging |
| Inbound | `policyService` | service call | Validate product/plan ACTIVE on policy create/issue |
| Inbound | UI `/product-management` | REST | Catalog desk + prem-rules |

## Provided ports

- Product CRUD + search; plans/coverages under product
- Lifecycle: `/{id}/submit|approve|activate|suspend|retire`
- Rules: `/{id}/rules/eligibility`, `/{id}/rules/pricing`, `/{id}/channels`
- Policy helper: `/{id|code}/for-policy-creation`

## Features

See [ProductManagementPlan.md](../../../../ProductManagementPlan.md) at repo root.

## Review & test

- [review-criteria.md](review-criteria.md)
- [test-plan.md](test-plan.md)

## UAT matrix (catalog linkage)

1. Create product → add plan → submit → approve → activate  
2. Issue policy only for ACTIVE product + valid plan code  
3. Prem-rules / premium calc select product+plan from catalog  
4. Commission rule shows product name (not raw UUID)  
5. Upgrade/downgrade plan list scoped to policy’s product  
