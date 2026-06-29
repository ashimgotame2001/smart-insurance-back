# Solution Layer Mapping Skill

## Purpose

Map tenant- or deployment-specific requirements to Solution modules without polluting Product code.

## Solution Module Contains

- Adapter implementations for external partners
- Runtime configuration (`@ConfigurationProperties`)
- Strategy overrides registered via Spring `@Primary` or `@Qualifier`
- Locale/tenant-specific validation extensions
- Feature flag driven behavior

## Mapping Process

1. Identify Product interface or extension point
2. Implement Solution-specific class in `solution-*` module
3. Wire via Spring configuration profile or property flag
4. Keep Product default for tenants without override

## Example

```
Product: PayoutRoutingService (interface + default)
Solution: BankAPayoutRoutingAdapter implements PayoutRoutingService
Config: @ConditionalOnProperty("solution.payout.provider=bank-a")
```

See [service-runtime-switching-skill.md](service-runtime-switching-skill.md).
