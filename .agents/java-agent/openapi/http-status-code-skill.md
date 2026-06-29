# HTTP Status Code Skill

## Standard Mapping

| Scenario | Status |
|----------|--------|
| Successful GET | 200 OK |
| Successful POST (created) | 201 Created |
| Successful PUT/PATCH | 200 OK |
| Successful DELETE | 204 No Content |
| Validation error | 400 Bad Request |
| Missing auth | 401 Unauthorized |
| Forbidden | 403 Forbidden |
| Not found | 404 Not Found |
| Conflict / duplicate | 409 Conflict |
| Business rule violation | 422 Unprocessable Entity (if Platform supports) or 400 |
| Integration failure | 502 Bad Gateway |
| Unexpected error | 500 Internal Server Error |

## OpenAPI Documentation

Every operation must document:

- Success response with schema
- Standard error responses (400, 404, 409, 500)
- Reference shared `GlobalResponse` error shape from `GlobalExceptionHandlerBaseController`

## Rules

- Do not return 200 with error body
- 201 responses include `Location` header when applicable
- 204 responses have empty body
