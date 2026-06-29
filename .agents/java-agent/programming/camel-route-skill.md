# Camel Route Skill

## Purpose

Define enterprise integration flows using **Apache Camel 4.x** with Spring Boot in **`edx-enterprise-integration`**.

Use Camel for orchestrated partner integrations, protocol mediation, and multi-step routing — not for simple single-call REST adapters (use [adapter-design-pattern-skill](../code-generation/adapter-design-pattern-skill.md) / WebClient instead).

## Platform Module

| Module | Package | Role |
|--------|---------|------|
| `edx-enterprise-integration` | `com.swifttech.edx.ei` | Camel routes, enterprise connectors |

## Dependencies (reference)

```gradle
api 'org.apache.camel.springboot:camel-spring-boot-starter:4.13.0'
api 'org.apache.camel.springboot:camel-http-starter:4.13.0'
api 'org.apache.camel.springboot:camel-rest-starter:4.13.0'
api 'org.apache.camel.springboot:camel-jackson-starter:4.13.0'
```

Add additional Camel components (Kafka, JPA, etc.) only when the route requires them.

## Package Layout

```
com.swifttech.edx.ei
├── route/
│   ├── RemittancePayoutRoute.java      # extends BaseRouteBuilder
│   └── PartnerWebhookRoute.java
├── route/base/
│   └── BaseRouteBuilder.java           # shared onException, headers, conventions
├── processor/
│   └── RemittanceTransformProcessor.java
├── config/
│   └── CamelRouteConfig.java
└── EdxEnterpriseIntegrationApplication.java
```

## BaseRouteBuilder Pattern

```java
public abstract class BaseRouteBuilder extends RouteBuilder {

    @Override
    public final void configure() {
        onException(GlobalException.class)
            .handled(true)
            .process(this::mapGlobalExceptionToResponse);
        configureRoutes();
    }

    protected abstract void configureRoutes();

    private void mapGlobalExceptionToResponse(Exchange exchange) {
        GlobalException ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, GlobalException.class);
        exchange.getMessage().setBody(
            ServiceResponseBuilder.buildFailResponse(ex.getCode(), ex.getMessage())
        );
    }
}
```

Concrete routes implement `configureRoutes()` only.

## Route Pattern

```java
@Component
public class RemittancePayoutRoute extends BaseRouteBuilder {

    @Override
    protected void configureRoutes() {
        from("direct:initiatePayout")
            .routeId("remittance-payout-route")
            .log("Payout initiated correlationId=${header.correlationId}")
            .process(remittanceTransformProcessor)
            .marshal().json()
            .to("http://partner-api/payout?bridgeEndpoint=true")
            .unmarshal().json(PayoutResponse.class)
            .to("direct:postPayoutSuccess");
    }
}
```

## Rules

1. **Solution / integration module** owns partner-specific routes; Product exposes `direct:` entry points or service interfaces
2. Extend **`BaseRouteBuilder`** (platform base class); register concrete routes as `@Component` — Spring Boot auto-discovers routes
3. Put shared `onException`, correlation ID, and error mapping in `BaseRouteBuilder.configure()` — not duplicated per route
4. Set explicit **`routeId`** on every route for observability and debugging
5. Use **`direct:`** for in-vm orchestration between routes in the same application
6. Use **`http:`** / **`rest:`** for external systems; configure timeouts and `bridgeEndpoint=true` where needed
7. Marshal/unmarshal with **Jackson** (`camel-jackson-starter`) — do not pass raw JSON strings through service layer
8. Map partner errors in `BaseRouteBuilder` `onException` or dedicated `Processor` → `GlobalException`
9. Propagate **correlation ID** in Camel headers (`exchange.getIn().setHeader("correlationId", ...)`)
10. Keep business rules out of route DSL — use `Processor` or delegate to application service beans

## When to Use Camel vs WebClient Adapter

| Scenario | Approach |
|----------|----------|
| Single request/response to partner | WebClient adapter |
| Multi-step integration (transform → call → retry → notify) | Camel route |
| Protocol switch (REST in, MQ out) | Camel route |
| Scheduled / polled partner fetch | Camel route with timer/quartz |
| CRUD REST API for UI | Spring `@RestController` |

## Error Handling

- Use `onException(...)` with `GlobalException` and HTTP-specific exceptions
- Do not swallow errors — log and map to `GlobalResponse` fail shape
- Enable dead-letter handling for async routes when delivery must be retried

## Observability

- Use `.log()` with correlation and routeId at key steps
- Register route metrics via Micrometer Camel component when enabled
- Name processors clearly: `*Processor`, `*Transformer`

## Layer Placement

| Layer | Camel artifact |
|-------|----------------|
| Platform | `edx-enterprise-integration` module, `BaseRouteBuilder`, shared processors |
| Product | Generic route templates, `direct:` contracts |
| Solution | Partner-specific URLs, auth headers, route overrides |

## Testing

- Use `CamelTestSupport` or `@CamelSpringBootTest` for route unit tests
- Mock external endpoints with `AdviceWith` or embedded HTTP test support
- Verify transform processors independently of route DSL

## Anti-Patterns

| Pattern | Why |
|---------|-----|
| Business logic only in XML/DSL strings | Hard to test; use Java processors |
| Route calling JPA repository directly | Delegate to application service |
| Duplicate WebClient + Camel for same partner call | Pick one integration style |
| Extend Camel `RouteBuilder` directly in feature routes | Use `BaseRouteBuilder` for shared error handling |
| Missing `routeId` | Breaks ops tracing |

## Related

- [../code-generation/adapter-design-pattern-skill.md](../code-generation/adapter-design-pattern-skill.md)
- [event-driven-skill.md](event-driven-skill.md)
- [exception-handling-skill.md](exception-handling-skill.md)
- [observability-skill.md](observability-skill.md)
- [../functional-analysis/integration-flow-analysis-skill.md](../functional-analysis/integration-flow-analysis-skill.md)
