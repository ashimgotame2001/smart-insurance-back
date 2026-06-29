# Event Template

```java
@Getter
@AllArgsConstructor
public class RemittanceSubmittedEvent {
    private final Long remittanceUid;
    private final Long customerId;
    private final String status;
    private final Instant occurredAt;
}

@Component
@RequiredArgsConstructor
public class RemittanceEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishSubmitted(RemittanceTransaction entity) {
        publisher.publishEvent(new RemittanceSubmittedEvent(
            entity.getUid(),
            entity.getCustomerId(),
            entity.getStatus().name(),
            Instant.now()
        ));
    }
}

@Component
@Slf4j
public class RemittanceSubmittedHandler {

    @EventListener
    public void on(RemittanceSubmittedEvent event) {
        log.info("Handling RemittanceSubmitted remittanceUid={}", event.getRemittanceUid());
    }
}
```

Rules: publish after successful transaction; minimal payload; idempotent consumers.
