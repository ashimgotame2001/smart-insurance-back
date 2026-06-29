# Platform Compliance Review

## Check

- [ ] Platform utilities reused (pagination, exceptions, audit base)
- [ ] No duplicated Platform classes in Product modules
- [ ] Errors thrown as `GlobalException` only; handler is `GlobalExceptionHandlerBaseController`
- [ ] `GlobalResponse` used for error payloads
- [ ] Security filters and auth patterns from Platform applied
- [ ] Tracing/logging uses Platform MDC helpers
- [ ] Configuration follows Platform property conventions

## Violations

| Finding | Severity |
|---------|----------|
| Custom exception class instead of `GlobalException` | Major |
| Copied `GlobalException` / handler into Product module | Major |
| Custom pagination DTO ignoring Platform standard | Minor |
| Missing correlation ID in logs | Major |
