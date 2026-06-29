# Security Skill

## Purpose

Apply authentication, authorization, and data protection in Smart Remittance APIs.

## Rules

- All external APIs secured unless explicitly public health endpoints
- Use Spring Security method-level `@PreAuthorize` on sensitive service methods
- Validate resource ownership (customer can only access own remittances)
- Redact PII in logs (account numbers, ID numbers)
- Never log tokens or passwords

## Authorization Patterns

| Pattern | Usage |
|---------|--------|
| Role-based | `@PreAuthorize("hasRole('TELLER')")` |
| Permission-based | `@PreAuthorize("hasAuthority('remittance:create')")` |
| Resource-based | Check entity ownership in service layer |

## Input Security

- Validate and sanitize all inputs
- Use parameterized queries (JPA default)
- Limit payload size for upload endpoints

See [../review/security-review.md](../review/security-review.md) and [../checklists/security-review-checklist.md](../checklists/security-review-checklist.md).
