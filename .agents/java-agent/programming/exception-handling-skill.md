# Exception Handling Skill

## Purpose

Map domain and integration failures to consistent HTTP error responses using **`GlobalException`** only, handled by **`GlobalExceptionHandlerBaseController`**.

**Reference implementation:**

- `com.swifttech.edx.dm.exception`
- `com.swifttech.edx.dm.model.response`

## Platform Components

| Component | Package | Role |
|-----------|---------|------|
| `GlobalException` | `com.swifttech.edx.dm.exception` | Single runtime exception for business and integration failures |
| `GlobalExceptionHandlerBaseController` | `com.swifttech.edx.dm.exception.handler` | `@ControllerAdvice` — maps exceptions to `GlobalResponse` |
| `GlobalResponse<T>` | `com.swifttech.edx.dm.model.response` | Standard API wrapper (success and error) |
| `ErrorDetail` | `com.swifttech.edx.dm.model.response` | Structured error block |
| `ErrorCause` | `com.swifttech.edx.dm.model.response` | Grouped validation causes (`FIELD` / `OBJECT`) |
| `CauseDetail` | `com.swifttech.edx.dm.model.response` | Per-field or per-object error detail |
| `MessageHelper` | `com.swifttech.edx.dm.util` | Resolves error messages from error codes |
| `ServiceResponseBuilder` | `com.swifttech.edx.dm.builder` | Builds `GlobalResponse` success/fail payloads |

## Rules

- **Throw only `GlobalException`** from service/application layer — do not create module-specific exception classes
- Do not catch `GlobalException` in controllers; let `GlobalExceptionHandlerBaseController` handle it
- Use stable **error codes** in `AAA-BBB-OPR-NNN` format — see [message-code-convention-skill.md](message-code-convention-skill.md)
- Codes resolved via `MessageHelper` (e.g. `ErrorCodeEnum`, module-specific codes)
- Set **`HttpStatus`** explicitly when the default `422 UNPROCESSABLE_CONTENT` is not correct
- Jakarta Validation failures (`@Valid`) are handled automatically — do not throw `GlobalException` for field validation errors
- Never expose stack traces, SQL, or internal details in `debugMessage` returned to clients
- Log failures with correlation ID at service or handler level

## `GlobalException` Constructors

```java
// Code only — message from MessageHelper; default 422 UNPROCESSABLE_CONTENT
new GlobalException("RMS-RMS-CRE-001")

// Code + custom message — default 422 UNPROCESSABLE_CONTENT
new GlobalException("RMS-RMS-CRE-001", "Remittance not found")

// Code + explicit HTTP status
new GlobalException("RMS-RMS-GET-001", HttpStatus.NOT_FOUND)

// Full control: debug message, HTTP status, API status, code
new GlobalException("debug detail", HttpStatus.CONFLICT, ApiStatusEnum.FAILED, "RMS-RMS-UPD-001")
```

## HTTP Status Guidance

Pass `HttpStatus` on `GlobalException` for non-default cases:

| Scenario | `HttpStatus` |
|----------|--------------|
| Entity not found | `NOT_FOUND` (404) |
| Duplicate / constraint conflict | `CONFLICT` (409) |
| Permission / tenant denied | `FORBIDDEN` (403) |
| Business rule violation | `UNPROCESSABLE_CONTENT` (422) — default |
| Invalid request payload (manual check) | `BAD_REQUEST` (400) |
| External system failure | `BAD_GATEWAY` (502) |
| Unexpected failure | `INTERNAL_SERVER_ERROR` (500) |

## Service Layer Examples

```java
return repository.findById(id)
    .orElseThrow(() -> new GlobalException("RMS-RMS-GET-001", HttpStatus.NOT_FOUND));

if (amountExceedsLimit(request.getAmount())) {
    throw new GlobalException("RMS-RMS-CRE-001", "Amount exceeds corridor limit");
}

if (partnerCallFailed) {
    throw new GlobalException(
        "Partner timeout",
        HttpStatus.BAD_GATEWAY,
        ApiStatusEnum.FAILED,
        "RMS-RMS-CRE-002"
    );
}
```

## Handler Behavior

`GlobalExceptionHandlerBaseController`:

- Catches `GlobalException` and returns `ResponseEntity<GlobalResponse>` with `e.getHttpStatus()`
- Builds response code using `ResponseCodeAspect` + module prefix when code is numeric
- Resolves user-facing message via `MessageHelper.getErrorMessage(finalCode)`
- Handles validation (`MethodArgumentNotValidException`, `HandlerMethodValidationException`) separately with `ErrorDetail` / `CauseDetail`
- Falls back to `buildUnknownFailResponse` for unhandled `Exception` (500)

## Response Model (`com.swifttech.edx.dm.model.response`)

### `GlobalResponse<T>`

| Field | Type | JSON | Notes |
|-------|------|------|-------|
| `status` | `String` | yes | e.g. `"200"` success, `"400"` fail (`Constant.FAILED_STATUS`), or `"FAILED"` on validation |
| `httpStatus` | `HttpStatus` | **no** | `@JsonIgnore` — used server-side only |
| `code` | `String` | yes | Module-prefixed error/success code |
| `data` | `T` | yes | Payload; `null` on error |
| `message` | `String` | yes | User-facing message from `MessageHelper` or handler |
| `errorDetail` | `ErrorDetail` | yes | Present for validation and some fail responses |

### `ErrorDetail`

| Field | Type | Description |
|-------|------|-------------|
| `code` | `String` | Error category (e.g. `"ERROR"`, `"VALIDATION_ERROR"`) |
| `message` | `String` | Summary message |
| `description` | `String` | Optional longer description |
| `causes` | `List<ErrorCause>` | Validation cause groups |

### `ErrorCause`

| Field | Type | Description |
|-------|------|-------------|
| `type` | `String` | `"FIELD"` or `"OBJECT"` |
| `details` | `List<CauseDetail>` | Individual cause entries |

### `CauseDetail`

| Field | Type | Description |
|-------|------|-------------|
| `entityName` | `String` | Validated object name |
| `attributeName` | `String` | Field name (`null` for object-level errors) |
| `code` | `String` | Module-prefixed field error code |
| `message` | `String` | Resolved validation message |

## Error Response Examples

### 1. `GlobalException` (business / integration error)

HTTP status from `GlobalException.getHttpStatus()`. Body from `ServiceResponseBuilder.buildFailResponse(finalCode, message)`:

```json
{
  "status": "400",
  "code": "RMSRemittanceControllercreate001",
  "message": "Remittance not found",
  "data": null
}
```

`code` is built by `ResponseCodeAspect` (module + controller + method prefix) + numeric/string error code when applicable.

### 2. Jakarta Validation (`@Valid` / `MethodArgumentNotValidException`)

HTTP `400 Bad Request`. Body includes nested `errorDetail` and `causes`:

```json
{
  "status": "FAILED",
  "code": "400",
  "message": "Bad Request",
  "data": null,
  "errorDetail": {
    "code": "VALIDATION_ERROR",
    "message": "Request validation failed",
    "description": "One or more fields failed validation.",
    "causes": [
      {
        "type": "FIELD",
        "details": [
          {
            "entityName": "createRemittanceRequest",
            "attributeName": "amount",
            "code": "RMSRemittanceControllercreateREM012",
            "message": "Amount must be positive"
          }
        ]
      },
      {
        "type": "OBJECT",
        "details": [
          {
            "entityName": "createRemittanceRequest",
            "attributeName": null,
            "code": "RMSRemittanceControllercreateREM020",
            "message": "Beneficiary account is invalid"
          }
        ]
      }
    ]
  }
}
```

### 3. Unhandled `Exception`

HTTP `500 Internal Server Error`:

```json
{
  "status": "400",
  "message": "Internal error message",
  "errorDetail": {
    "code": "ERROR",
    "message": "Internal error message"
  }
}
```

### 4. `GlobalException` via `buildFailResponse(GlobalException e)`

Includes `errorDetail` with code `"ERROR"`:

```json
{
  "status": "400",
  "code": "REM001",
  "message": "Resolved error message",
  "errorDetail": {
    "code": "ERROR",
    "message": "Resolved error message"
  }
}
```

## OpenAPI / Code Generation Rules

- Document error responses using `GlobalResponse` shape — not a custom `ErrorResponse` schema
- Reference `ErrorDetail`, `ErrorCause`, `CauseDetail` for validation error responses
- Success responses also use `GlobalResponse<T>` with `status`, `code`, `message`, `data`
- See [../templates/openapi-template.yaml](../templates/openapi-template.yaml) and [../openapi/request-response-schema-skill.md](../openapi/request-response-schema-skill.md)

## Controller Layer

- No local `try/catch` for business errors
- Rely on `GlobalExceptionHandlerBaseController`
- Return success payloads via `GlobalResponse` / `ServiceResponseBuilder` success builders

## Related

- [message-code-convention-skill.md](message-code-convention-skill.md)
- [../review/logging-review.md](../review/logging-review.md)
- [client-adapter-pattern-skill.md](client-adapter-pattern-skill.md)
