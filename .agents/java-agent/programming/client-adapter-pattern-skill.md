# Client Adapter Pattern Skill

## Purpose

Encapsulate outbound calls to external systems behind adapter interfaces.

## Structure

```
domain/port/OutboundPaymentPort.java     ← interface
infrastructure/adapter/PaymentClientAdapter.java  ← WebClient/gRPC impl
```

## Rules

- Adapter implements port defined in domain or application layer
- Map external request/response to internal DTOs in adapter
- Handle timeouts, retries, and circuit breaking in adapter
- Never leak partner JSON structures into service layer
- Log outbound calls with correlation ID; redact sensitive fields

## Error Handling

| Partner response | Map to |
|------------------|--------|
| 4xx client error | `GlobalException` with `HttpStatus.BAD_REQUEST` or `UNPROCESSABLE_CONTENT` |
| 5xx / timeout | `GlobalException` with `HttpStatus.BAD_GATEWAY` |
| Unknown | `GlobalException` with sanitized code/message |

## Placement

- Generic port interface → Product
- Partner-specific adapter → Solution

## Related

- [grpc-client-adapter-skill.md](grpc-client-adapter-skill.md) — internal gRPC outbound calls
- [../code-generation/adapter-design-pattern-skill.md](../code-generation/adapter-design-pattern-skill.md)
