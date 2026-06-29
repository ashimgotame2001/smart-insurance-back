# Smart Remittance Module Map

Gradle multi-module layout for Smart Remittance backend services. Use this map to place new code in the correct module.

## Layer Overview

| Layer | Gradle Pattern | Responsibility |
|-------|----------------|----------------|
| Platform | `platform-*` | Cross-cutting infra: security, logging, tracing, base exceptions, pagination, event bus abstractions |
| Product | `sr-*` (product modules) | Reusable remittance domain capabilities shared across solutions |
| Solution | `solution-*` | Tenant-specific rules, adapters, runtime configuration, feature toggles |

## Product Modules

| Module | Primary Domain |
|--------|----------------|
| `sr-customer-management` | Customer onboarding, KYC profile, customer lifecycle |
| `sr-beneficiary-management` | Beneficiary registration, validation, relationship |
| `sr-remittance-services` | Remittance transaction orchestration, send/receive flows |
| `sr-aml-compliance` | AML screening, sanctions, compliance holds |
| `sr-forex-services` | FX rates, quotes, conversion |
| `sr-payment-services` | Payment initiation, settlement, payment rails |
| `sr-accounting` | Ledger entries, accounting events |
| `sr-revenue-management` | Fees, commissions, revenue sharing |
| `sr-product-services` | Remittance product catalog, corridors, limits |
| `sr-service-delivery-network` | Payout network, agent/partner routing |
| `sr-teller-services` | Branch/teller operations, cash handling |
| `sr-virtual-account` | Virtual account provisioning and balance |
| `sr-reporting-services` | Reports, exports, regulatory reporting |

## Package Convention (per module)

```
com.edx.platform.sr.<module-short-name>
├── api/              # Controllers implementing OpenAPI
├── application/      # Services, use cases
├── domain/           # Entities, domain services, repository interfaces
│   ├── entity/       # JPA entities
│   ├── projection/   # Read/query projections for list and summary APIs
│   └── repository/   # Repository interfaces
├── infrastructure/   # JPA repos, adapters, event handlers
├── dto/              # Request/response DTOs
├── mapper/           # MapStruct or manual mappers
├── validator/        # Custom validators
└── config/           # Module-specific Spring config
```

## Placement Rules

1. **Shared behavior across all solutions** → Product module (`sr-*`)
2. **Tenant-specific rule or integration** → Solution module (`solution-*`)
3. **Generic utility not remittance-specific** → Platform module (`platform-*`)
4. **Cross-module orchestration** → calling module's application layer via defined client adapter; do not import another module's infrastructure directly

## Module Selection Checklist

Before coding, confirm:

- [ ] Target module identified from table above
- [ ] Layer placement (Platform / Product / Solution) confirmed
- [ ] No Solution-specific logic placed in Product module
- [ ] Platform utilities reused instead of duplicated
