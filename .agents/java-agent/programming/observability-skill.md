# Observability Skill

## Purpose

Ensure every service operation is traceable in logs and distributed tracing systems.

## Requirements

- Propagate correlation/trace ID from incoming request to outbound calls
- Structured JSON logging
- Log at INFO for business milestones; DEBUG for diagnostic detail
- Span naming: `<module>.<operation>` (e.g. `remittance.submit`)

## Log Fields

Include where applicable:

- `traceId`, `spanId`
- `module`, `operation`
- Business IDs (remittanceId, customerId) — not PII
- `durationMs` for slow operations

## Tracing

- Annotate service entry points
- Tag spans with `module` and `tenant` when available

See [../traceability/observability-traceability.md](../traceability/observability-traceability.md).
