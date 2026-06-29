# Integration Flow Analysis Skill

## Purpose

Analyze inbound and outbound integrations (REST, gRPC, message queues, partner APIs) and map them to client adapters.

## For Each Integration

| Aspect | Document |
|--------|----------|
| Direction | Inbound API vs outbound client |
| Protocol | REST, gRPC, Kafka, Camel |
| Trigger | Sync call vs event-driven |
| Timeout / retry | Resilience requirements |
| Error mapping | Partner errors → Platform exceptions |
| Security | mTLS, API keys, OAuth |

## Placement

- Generic adapter interface → Product module
- Partner-specific implementation → Solution module
- HTTP client config, circuit breaker → Platform or shared config
- Multi-step partner orchestration → Camel route ([../programming/camel-route-skill.md](../programming/camel-route-skill.md))

See [../code-generation/adapter-design-pattern-skill.md](../code-generation/adapter-design-pattern-skill.md).

For gRPC outbound integrations, also load [../programming/grpc-client-adapter-skill.md](../programming/grpc-client-adapter-skill.md).
