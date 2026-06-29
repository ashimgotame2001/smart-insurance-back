# Request Response Schema Skill

## Purpose

Define consistent OpenAPI schemas for requests and responses.

## Naming

| Type | Pattern | Example |
|------|---------|---------|
| Create request | `Create{Resource}Request` | `CreateRemittanceRequest` |
| Update request | `Update{Resource}Request` | `UpdateBeneficiaryRequest` |
| Response | `{Resource}Response` | `RemittanceResponse` |
| List item | `{Resource}Summary` | `RemittanceSummary` |
| Error / success wrapper | `GlobalResponse` | `com.swifttech.edx.dm.model.response` |
| Error detail | `ErrorDetail` | nested in `GlobalResponse.errorDetail` |
| Validation cause | `ErrorCause` + `CauseDetail` | nested in `ErrorDetail.causes` |

## `GlobalResponse` Schema

```yaml
GlobalResponse:
  type: object
  properties:
    status:
      type: string
      description: API status string (e.g. "200", "400", "FAILED")
    code:
      type: string
      description: Module-prefixed response or error code
    message:
      type: string
    data:
      type: object
      nullable: true
      description: Endpoint-specific success payload
      additionalProperties: true
    errorDetail:
      $ref: '#/components/schemas/ErrorDetail'

ErrorDetail:
  type: object
  properties:
    code:
      type: string
    message:
      type: string
    description:
      type: string
    causes:
      type: array
      items:
        $ref: '#/components/schemas/ErrorCause'

ErrorCause:
  type: object
  properties:
    type:
      type: string
      enum: [FIELD, OBJECT]
    details:
      type: array
      items:
        $ref: '#/components/schemas/CauseDetail'

CauseDetail:
  type: object
  properties:
    entityName:
      type: string
    attributeName:
      type: string
      nullable: true
    code:
      type: string
    message:
      type: string
```

Align with [../programming/exception-handling-skill.md](../programming/exception-handling-skill.md).

## Schema Rules

- Required fields listed in `required` array
- Use `format: int64` for IDs
- Money: object with `amount` (number) + `currency` (ISO 4217 string)
- Dates: `format: date-time` (UTC)
- Enums: align with Java `enum` names

## Response Wrapping

- All API responses use `GlobalResponse<T>` with `status`, `code`, `message`, `data`
- Errors include `errorDetail` (`ErrorDetail` → `ErrorCause` → `CauseDetail`) when validation fails
- List data may be wrapped in `data` or a dedicated page type per module convention

See [pagination-filtering-sorting-skill.md](pagination-filtering-sorting-skill.md).
