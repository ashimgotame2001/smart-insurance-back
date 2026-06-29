# Update Existing API

Use the Smart Remittance Java Agent skill.

## Task

Modify an existing Smart Remittance API (fields, behavior, status codes).

## Inputs

- Module and API path/operationId:
- Change description:
- OpenAPI file path:
- Backward compatibility requirement: yes/no

## Instructions

1. Diff functional impact (new validation, breaking changes)
2. Update OpenAPI first
3. Update DTOs, mapper, service, tests
4. Flag breaking changes explicitly
5. Update traceability matrix

## Output

Change summary + updated files + migration notes if breaking
