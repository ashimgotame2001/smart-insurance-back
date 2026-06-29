# Adapter Design Pattern Skill

## Purpose

Design outbound client adapters that encapsulate calls to external systems behind port interfaces.

## Rules

- Implement port interface from domain/application layer
- Use WebClient or gRPC stub internally
- Configure timeout, retry via Platform resilience utilities
- Map external models to internal DTOs
- Place partner-specific impl in Solution module
- Never leak partner JSON structures into service layer

## Structure

```
domain/port/OutboundPaymentPort.java     ← interface
infrastructure/adapter/PaymentClientAdapter.java  ← WebClient/gRPC impl
```

## Template

See [../templates/adapter-template.md](../templates/adapter-template.md).

## Related

- [../programming/client-adapter-pattern-skill.md](../programming/client-adapter-pattern-skill.md)
- [../programming/grpc-client-adapter-skill.md](../programming/grpc-client-adapter-skill.md)
