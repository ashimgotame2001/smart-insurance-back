# Figma to Backend Traceability

## Mapping Table

For each feature, maintain:

| Figma Frame ID | Screen Name | Field / Action | API operationId | DTO Field | Validation Rule |
|----------------|-------------|----------------|-----------------|-----------|-----------------|

## Rules

- Every mandatory UI field maps to a DTO property with validation
- Every primary button maps to an API operation
- Status labels map to entity enum values
- Error text on UI maps to validation message key or error code

See [../functional-analysis/figma-analysis-skill.md](../functional-analysis/figma-analysis-skill.md).
