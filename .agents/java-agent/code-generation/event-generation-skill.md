# Event Generation Skill

## Deliverables

- Event class in `domain` or `infrastructure.event`
- Publisher wrapper using Platform event bus
- Consumer `@KafkaListener` or handler in subscribing module
- Serialization-compatible payload (JSON)

## Event Class Template

```java
@Getter
@AllArgsConstructor
public class RemittanceSubmittedEvent {
    private final Long remittanceUid;
    private final Long customerId;
    private final String status;
    private final Instant occurredAt;
}
```

## Template

See [../templates/event-template.md](../templates/event-template.md).

See [../programming/event-driven-skill.md](../programming/event-driven-skill.md).
