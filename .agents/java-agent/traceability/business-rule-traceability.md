# Business Rule Traceability

## Rule Registry

| Rule ID | Description | Source (FRD section) | Enforced In | Error Code |
|---------|-------------|----------------------|-------------|------------|

## Enforcement Locations

| Location | Suitable for |
|----------|--------------|
| DTO validation | Format, required, range |
| Custom validator | Cross-field rules |
| Service | State guards, uniqueness |
| Domain entity | Invariant within aggregate |

## Rules

- Each business rule has unique stable ID (e.g. `BR-REM-012`)
- Same rule ID in OpenAPI description, validator, and tests
- Document Solution-specific rules separately with solution prefix
