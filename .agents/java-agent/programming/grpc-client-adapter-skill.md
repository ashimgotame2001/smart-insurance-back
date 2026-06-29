# gRPC Client Adapter Skill

## Purpose

Implement outbound gRPC calls to internal Smart Remittance or Platform services behind port interfaces, with consistent error mapping and observability.

Use for high-performance **internal** service-to-service calls. External partner APIs remain REST/WebClient unless explicitly specified as gRPC.

## When to Use

- Integration analysis identifies gRPC as the protocol ([integration-flow-analysis-skill.md](../functional-analysis/integration-flow-analysis-skill.md))
- Calling another SR module or Platform service that exposes a gRPC stub
- Latency-sensitive sync calls where REST is not the contract

## Structure

```
domain/port/ForexQuotePort.java              ← interface (Product)
infrastructure/adapter/ForexGrpcClientAdapter.java  ← stub wrapper
config/GrpcClientConfiguration.java        ← channel, stub beans
```

Product module owns the **port** and a default or internal gRPC adapter. Solution module supplies partner- or tenant-specific stub configuration when endpoints or credentials differ.

## Dependencies (reference)

Add to the consuming module's `build.gradle`:

```gradle
implementation project(':platform-grpc-client')   // if Platform provides shared gRPC utilities
implementation 'io.grpc:grpc-netty-shaded'
implementation 'io.grpc:grpc-protobuf'
implementation 'io.grpc:grpc-stub'
implementation 'net.devh:grpc-client-spring-boot-starter'  // when using grpc-spring-boot
```

Generated protobuf stubs come from the **provider module's** proto artifact — do not copy `.proto` files into consumer modules.

## Configuration

Externalize channel settings via `@ConfigurationProperties`:

```java
@ConfigurationProperties(prefix = "sr.forex.grpc")
public record ForexGrpcProperties(
    String host,
    int port,
    Duration deadline,
    boolean tlsEnabled
) {}
```

| Setting | Rule |
|---------|------|
| Deadline | Always set per-call or channel default — never unbounded |
| TLS | Required for non-local environments |
| Retry | Use Platform resilience policy; idempotent reads only |
| Load balancing | Follow Platform service discovery when available |

## Adapter Implementation

```java
@Component
@RequiredArgsConstructor
@Slf4j
public class ForexGrpcClientAdapter implements ForexQuotePort {

    private final ForexQuoteServiceGrpc.ForexQuoteServiceBlockingStub stub;
    private final ForexGrpcMapper mapper;
    private final ForexGrpcProperties properties;

    @Override
    public QuoteResult getQuote(QuoteRequest request) {
        var grpcRequest = mapper.toGrpc(request);
        try {
            var response = stub
                .withDeadlineAfter(properties.deadline().toMillis(), TimeUnit.MILLISECONDS)
                .getQuote(grpcRequest);
            return mapper.toDomain(response);
        } catch (StatusRuntimeException ex) {
            log.warn("Forex gRPC call failed status={} description={}",
                ex.getStatus().getCode(), ex.getStatus().getDescription());
            throw mapToGlobalException(ex);
        }
    }

    private GlobalException mapToGlobalException(StatusRuntimeException ex) {
        return switch (ex.getStatus().getCode()) {
            case NOT_FOUND -> new GlobalException("FXR-FXR-GET-001", HttpStatus.NOT_FOUND);
            case INVALID_ARGUMENT -> new GlobalException("FXR-FXR-GET-002", HttpStatus.BAD_REQUEST);
            case DEADLINE_EXCEEDED, UNAVAILABLE ->
                new GlobalException("FXR-FXR-GET-503", HttpStatus.BAD_GATEWAY);
            default -> new GlobalException("FXR-FXR-GET-500", HttpStatus.BAD_GATEWAY);
        };
    }
}
```

## Rules

- Wrap generated stubs in an adapter class — services inject the **port**, not the stub
- Map protobuf messages to internal DTOs in the adapter or dedicated mapper — never pass protobuf types to the application layer
- Propagate trace context via Platform gRPC interceptors when available
- Log method name, correlation ID, and gRPC status — never log full request/response payloads with PII
- Use blocking stub for simple sync flows; async stub only when the calling service is already reactive or fire-and-forget

## Error Mapping

| gRPC status | HTTP / `GlobalException` |
|-------------|--------------------------|
| `NOT_FOUND` | `HttpStatus.NOT_FOUND` |
| `INVALID_ARGUMENT`, `FAILED_PRECONDITION` | `HttpStatus.BAD_REQUEST` or `UNPROCESSABLE_CONTENT` |
| `ALREADY_EXISTS` | `HttpStatus.CONFLICT` |
| `PERMISSION_DENIED`, `UNAUTHENTICATED` | `HttpStatus.FORBIDDEN` / `UNAUTHORIZED` |
| `DEADLINE_EXCEEDED`, `UNAVAILABLE`, `INTERNAL` | `HttpStatus.BAD_GATEWAY` |
| Unknown | Sanitized code + `BAD_GATEWAY` |

See [exception-handling-skill.md](exception-handling-skill.md) and [message-code-convention-skill.md](message-code-convention-skill.md).

## Placement

| Artifact | Layer |
|----------|-------|
| Port interface | Product (`domain/port` or `application/port`) |
| gRPC adapter + mapper | Product (internal SR call) or Solution (tenant-specific endpoint) |
| Stub/channel configuration | `config/` in the same module as the adapter |
| `.proto` definitions | Provider module only |

## Testing

- Unit-test adapter error mapping with mocked stub ([integration-test-generation-skill.md](../code-generation/integration-test-generation-skill.md))
- Contract tests against provider's proto version when available

## Related

- [client-adapter-pattern-skill.md](client-adapter-pattern-skill.md)
- [../code-generation/adapter-design-pattern-skill.md](../code-generation/adapter-design-pattern-skill.md)
- [../functional-analysis/integration-flow-analysis-skill.md](../functional-analysis/integration-flow-analysis-skill.md)
