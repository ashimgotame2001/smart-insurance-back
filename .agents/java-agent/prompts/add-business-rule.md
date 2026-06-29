# Add Business Rule

Use the Smart Remittance Java Agent skill.

## Task

Add or change a business rule in an existing module.

## Inputs

- Rule ID and description:
- Requirement reference:
- Target module and service:
- Error code and message:

## Instructions

1. Determine enforcement layer (validator vs service vs domain)
2. Assign rule ID ([business-rule-traceability.md](../traceability/business-rule-traceability.md))
3. Implement rule + tests for pass/fail cases
4. Update OpenAPI description if client-visible
5. Verify Product vs Solution placement

## Output

Rule implementation + test cases + trace row
