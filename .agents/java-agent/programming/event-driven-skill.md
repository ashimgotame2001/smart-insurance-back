# Event Driven Skill

## Purpose

Implement domain events and async handlers using Kafka or Platform event bus.

## Producer Pattern

```java
@Transactional
public RemittanceResponse submit(CreateRemittanceRequest request) {
    RemittanceTransaction entity = // save
    eventPublisher.publish(new RemittanceSubmittedEvent(entity.getUid(), entity.getStatus()));
    return mapper.toResponse(entity);
}
```

## Consumer Pattern

- `@KafkaListener` or Platform event handler in `infrastructure.event`
- Idempotent processing using event ID or business key
- Dead-letter handling for poison messages

## Event Design

- Past tense names: `RemittanceSubmitted`, `PaymentSettled`
- Minimal payload: IDs + changed attributes
- Version field for schema evolution

See [../code-generation/event-generation-skill.md](../code-generation/event-generation-skill.md).
