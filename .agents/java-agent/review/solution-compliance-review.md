# Solution Compliance Review

## Check

- [ ] Tenant-specific logic only in `solution-*` modules
- [ ] Overrides use `@ConditionalOnProperty`, `@Primary`, or strategy registry
- [ ] No Solution code duplicated across tenant modules unnecessarily
- [ ] Configuration externalized in properties/yaml
- [ ] Product default still works when Solution override absent

## Violations

| Finding | Severity |
|---------|----------|
| Solution adapter in Product module | Critical |
| Hardcoded credentials in Solution code | Critical |
| Override replaces Product without interface contract | Major |
