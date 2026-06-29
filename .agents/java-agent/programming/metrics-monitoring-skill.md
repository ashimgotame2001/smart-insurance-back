# Metrics Monitoring Skill

## Purpose

Expose Micrometer metrics for operational monitoring of remittance services.

## Standard Metrics

| Metric | Type | Labels |
|--------|------|--------|
| `http.server.requests` | Timer | method, uri, status |
| `remittance.submitted.total` | Counter | corridor, status |
| `integration.client.duration` | Timer | partner, operation |
| `db.query.duration` | Timer | repository method |

## Rules

- Use consistent metric naming prefix per module: `sr.remittance.*`
- Avoid high-cardinality labels (no user IDs)
- Register custom metrics in service layer for business events

See [../review/observability-review.md](../review/observability-review.md).
