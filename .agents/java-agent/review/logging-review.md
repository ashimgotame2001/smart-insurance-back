# Logging Review

## Check

- [ ] INFO for business events (created, submitted, settled)
- [ ] WARN for recoverable issues
- [ ] ERROR with stack trace only at service boundary / advice
- [ ] No PII or secrets logged
- [ ] Consistent log field names (`traceId`, `module`, `operation`)
- [ ] Appropriate log level (no DEBUG in hot paths at INFO)

## Anti-Patterns

| Pattern | Severity |
|---------|----------|
| Logging full request body with PII | Critical |
| `System.out.println` | Major |
| Generic "error occurred" without context | Minor |
