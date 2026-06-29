# Adapter Template

```java
public interface PayoutPort {
    PayoutResult route(PayoutRequest request);
}

@Component
@RequiredArgsConstructor
@Slf4j
public class PartnerPayoutAdapter implements PayoutPort {

    private final WebClient webClient;

    @Override
    public PayoutResult route(PayoutRequest request) {
        try {
            // map request → partner API → map response
            return new PayoutResult(/* ... */);
        } catch (WebClientResponseException ex) {
            log.warn("Payout partner error status={}", ex.getStatusCode());
            throw new GlobalException("PAY502", HttpStatus.BAD_GATEWAY);
        }
    }
}
```

Rules: port in domain/application; impl in infrastructure; Solution for partner-specific code.
