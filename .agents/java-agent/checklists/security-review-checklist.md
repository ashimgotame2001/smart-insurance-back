# Security Review Checklist

- [ ] Authentication on all non-public endpoints
- [ ] Authorization checks for role and resource ownership
- [ ] Input validation on all writes
- [ ] No hardcoded secrets
- [ ] PII masked in logs
- [ ] SQL injection prevented
- [ ] Rate limiting considered for sensitive endpoints
- [ ] External callbacks verified (signature/webhook secret)

See [../review/security-review.md](../review/security-review.md).
