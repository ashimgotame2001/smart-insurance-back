# Service Runtime Switching Skill

## Purpose

Switch service implementations at runtime based on solution configuration, tenant, or deployment profile.

## Patterns

### 1. Strategy interface (preferred)

```java
public interface FeeCalculationStrategy {
    Money calculate(FeeContext context);
}

@Service
@ConditionalOnProperty(name = "sr.fee.strategy", havingValue = "standard")
public class StandardFeeCalculationStrategy implements FeeCalculationStrategy { }

@Service
@ConditionalOnProperty(name = "sr.fee.strategy", havingValue = "premium")
public class PremiumFeeCalculationStrategy implements FeeCalculationStrategy { }
```

### 2. Factory with registry

- Product defines `StrategyFactory` keyed by solution code
- Solution modules register implementations at startup

### 3. `@Primary` override

- Product provides default `@Service`
- Solution module provides `@Primary` bean implementing same interface

## Rules

- Interface lives in Product module
- Default implementation in Product module
- Overrides in Solution module only
- Configuration externalized; no hardcoded tenant IDs in Product code
- Document active strategy in logs at startup (debug level)

## Review Checklist

- [ ] Product service has no tenant-specific branching
- [ ] Solution override is conditionally loaded
- [ ] Integration tests cover both default and override paths when critical

## Related

- [solution-layer-mapping-skill.md](solution-layer-mapping-skill.md)
- [../review/runtime-switching-review.md](../review/runtime-switching-review.md)
