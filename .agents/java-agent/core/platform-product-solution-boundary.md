# Platform / Product / Solution Boundary

## Definitions

| Layer | What It Is | Examples |
|-------|------------|----------|
| **Platform** | Shared infrastructure and generic utilities used across all products | Base exceptions, pagination, security filters, tracing, generic event bus wrapper |
| **Product** | Reusable Smart Remittance capabilities independent of a single tenant | Customer CRUD, remittance send flow, FX quote engine, AML screening interface |
| **Solution** | Tenant- or deployment-specific customization | Partner-specific payout adapter, locale-specific validation, feature flags per bank |

## Decision Tree

```
Is it generic infra with no remittance domain knowledge?
  YES → Platform
  NO ↓
Is it reusable across all Smart Remittance tenants/solutions?
  YES → Product (sr-*)
  NO ↓
Is it specific to one solution/tenant/integration?
  YES → Solution (solution-*)
```

## Placement Examples

| Concern | Layer | Reason |
|---------|-------|--------|
| `PageRequest` utility | Platform | Generic pagination |
| Create remittance transaction | Product | Core remittance capability |
| Route payout via Partner X API | Solution | Partner-specific adapter |
| Standard error response format | Platform | Cross-product convention |
| Beneficiary name validation (generic) | Product | Shared remittance rule |
| Beneficiary name format for Country Y | Solution | Locale/tenant override |

## Anti-Patterns

| Violation | Fix |
|-----------|-----|
| `if (tenant == "BANK_A")` in Product service | Move to Solution adapter or runtime strategy |
| Duplicate Platform exception class in Product | Import Platform module |
| Solution module contains generic customer CRUD | Move to `sr-customer-management` |
| Controller calls JPA repository directly | Add service layer |

## Runtime Switching

When the same Product interface must behave differently per solution:

1. Define interface in Product layer
2. Provide default Product implementation
3. Solution layer registers override via configuration or strategy pattern

See [../programming/service-runtime-switching-skill.md](../programming/service-runtime-switching-skill.md).

## Verification Questions

Before placing code, answer:

1. Would another Smart Remittance tenant need this unchanged? → Product
2. Is this only about how one tenant integrates or configures? → Solution
3. Is this unrelated to remittance domain? → Platform
