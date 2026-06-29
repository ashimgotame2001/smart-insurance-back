# Observability Traceability

## Purpose

Link operational signals to business capabilities for troubleshooting.

## Mapping

| Requirement | Metric / Log Event | Span Name | Alert Threshold |
|-------------|-------------------|-----------|-----------------|

## Rules

- Key business operations emit structured log at INFO with business IDs
- Custom metrics named after capability (e.g. `sr.remittance.submitted`)
- Trace spans named `<module>.<operation>` matching service methods
- Dashboards traceable to module owner via metric prefix

See [../programming/observability-skill.md](../programming/observability-skill.md).
