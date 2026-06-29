# API Traceability

## OpenAPI Extensions

```yaml
post:
  operationId: createRemittance
  description: |
    Requirement: REM-SEND-001
    Figma: REM-SEND-SCREEN-03
  x-requirement-id: REM-SEND-001
  x-figma-frame: REM-SEND-SCREEN-03
```

## Implementation Trace

Controller and service methods reference the same IDs in JavaDoc:

```java
/**
 * REM-SEND-001: Submit remittance for processing.
 * Figma: REM-SEND-SCREEN-03
 */
```

## Verification

- Every new operationId appears in trace matrix
- Tests named or tagged with requirement ID where possible
