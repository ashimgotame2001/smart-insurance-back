# API Path Standard Skill

## Purpose

Consistent REST URL design across Smart Remittance modules.

## Path Pattern

```
/api/v{version}/{module-resource}/{resource-id}/{sub-resource}
```

## Examples

```
GET    /api/v1/remittances
POST   /api/v1/remittances
GET    /api/v1/remittances/{remittanceId}
PATCH  /api/v1/remittances/{remittanceId}/status
GET    /api/v1/customers/{customerId}/beneficiaries
```

## Rules

- Plural nouns for collections
- kebab-case for multi-word resources
- No verbs in paths (use HTTP methods)
- Nest only when resource ownership is clear
- Version in path prefix (`/api/v1/`)

## Avoid

- `/api/createRemittance`
- `/api/v1/remittance/getById/{id}`
