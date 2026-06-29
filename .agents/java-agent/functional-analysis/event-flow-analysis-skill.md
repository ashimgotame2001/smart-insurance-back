# Event Flow Analysis Skill

## Purpose

Identify domain events, async handlers, and cross-module notifications from functional requirements.

## Identify Events When

- State change must notify other modules
- Downstream processing can be async
- Audit or analytics needs event stream
- Integration with external systems via message bus

## Event Specification Template

| Field | Value |
|-------|-------|
| Event name | PascalCase, past tense (e.g. `RemittanceSubmitted`) |
| Producer | Module and service |
| Payload | Key fields (IDs, timestamps, status) |
| Consumers | Module handlers |
| Delivery | At-least-once; idempotent handlers |

## Rules

- Events carry identifiers, not full entity graphs
- Publish after successful transaction commit
- Version event schema when breaking changes occur

See [../programming/event-driven-skill.md](../programming/event-driven-skill.md).
