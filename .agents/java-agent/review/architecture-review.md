# Architecture Review

## Check

- [ ] Controller → Service → Repository layering intact
- [ ] No business logic in controllers
- [ ] Entities not returned from APIs
- [ ] DTOs and mappers used at boundaries
- [ ] Events used for cross-module async communication
- [ ] Adapters encapsulate external integrations

## Layer Violations

| Pattern | Severity |
|---------|----------|
| Controller calls repository | Critical |
| Service returns Entity to controller without mapping | Major |
| Circular module dependency | Critical |

See [../core/architecture-approach.md](../core/architecture-approach.md).
