# Product Compliance Review

## Check

- [ ] Code in correct `sr-*` product module
- [ ] No tenant-specific branching in Product services
- [ ] Business rules are reusable across solutions
- [ ] Extension points defined for Solution overrides
- [ ] Module boundaries respected (no infra cross-imports)

## Violations

| Finding | Severity |
|---------|----------|
| `if (tenant.equals(...))` in Product service | Critical |
| Partner URL hardcoded in Product | Major |
| Generic CRUD in Solution that belongs in Product | Major |
