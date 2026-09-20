# Product Management Plan

Insurance product catalog workspace at `/product-management/*`: lifecycle, plans, coverages/riders/features, eligibility & rating rules, channel mapping, documents/versions, plus operational premium calc rules.

## Ownership

| Surface | Responsibility |
|---------|----------------|
| **Product Management** (`/product-management/*`) | Catalog CRUD, plans, coverages, riders, features, benefits, exclusions, categories, eligibility, rating rules, channels, documents, versions, lifecycle |
| **Premium Calc Rules** (`/product-management/prem-rules`) | Billing overlay rates by product/plan code (consumed by Premium Management calc) |
| **Policy / Premium / Commission** | Consume ACTIVE catalog via shared Product/Plan selectors; backend validates codes |

## Backend

- Facade: `/api/v1/products` → `ProductController` → `ProductService`
- Lifecycle: `submit` → `approve` → `activate` / `suspend` / `retire`
- Rules: eligibility, pricing (rating), channel mappings under product
- Policy helpers: `/{id|code}/for-policy-creation`
- Policy create/issue validates product ACTIVE + plan belongs to product

## Frontend

- Desk: products, plans, coverage setup, riders, features, benefits, exclusions, categories, documents, versions
- Rules UIs: eligibility, rating rules, channel mapping
- Shared: `ProductSelect` / `PlanSelect` used by policy edit/create/search, upgrade/downgrade, prem-rules, premium calc, commission

## Phases delivered

1. Menu consolidation + Features/Riders + lifecycle actions + this plan doc  
2. Eligibility / Rating / Channels UIs; prem-rules product/plan dropdowns  
3. Cross-module Product/Plan selects + PolicyService catalog validation  
4. Permissions, commission product display, MODULE.md refresh  

## Out of scope

Full Claims/UW/Quote modules, actuarial mortality tables, reinsurance cession.
