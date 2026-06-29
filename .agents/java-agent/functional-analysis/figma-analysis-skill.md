# Figma Analysis Skill

## Purpose

Derive API fields, validations, and user-flow-driven business rules from Figma or design prototype screens.

## Extract from Each Screen

| Design element | Backend implication |
|----------------|---------------------|
| Form fields | Request DTO properties, required/optional, max length |
| Dropdown options | Enum or reference data API |
| Buttons / actions | POST/PUT/PATCH endpoints |
| Tables / lists | GET with pagination, sort, filter params |
| Status badges | Entity status enum values |
| Error messages on UI | Validation messages and error codes |
| Conditional fields | Cross-field validation rules |
| Multi-step wizard | State machine or draft entity pattern |

## Workflow

1. List all screens in the user journey
2. Map screen → API operation(s)
3. Extract field list with types and constraints from design specs
4. Note navigation guards (e.g. cannot proceed without KYC)
5. Link screen IDs to traceability records

## Output

Screen-to-API mapping table:

| Screen / Frame | User Action | API | Notes |
|----------------|-------------|-----|-------|

See [../traceability/figma-to-backend-traceability.md](../traceability/figma-to-backend-traceability.md).
