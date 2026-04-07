/**
 * Proxy that guards access to an external (paid) service using rate limiting.
 *
 * This is the integration point described in the requirements:
 * rate limiting is applied only at the moment the system is about to
 * call the external resource — not at the incoming API level.
 *
 * Flow:
 *   1. Business logic decides an external call is needed
 *   2. Calls ExternalResourceProxy.callExternalResource(key, request)
 *   3. Proxy checks RateLimiterService.allowRequest(key)
 *   4. If allowed → delegates to the real ExternalService
 *   5. If denied  → throws RateLimitExceededException
 *
 * Design Pattern: Proxy Pattern (wraps ExternalService with rate-limit check)
 */
public class ExternalResourceProxy {

    private final RateLimiterService rateLimiter;
    private final ExternalService externalService;

    /**
     * Creates a rate-limited proxy for the given external service.
     *
     * @param rateLimiter    the rate limiter service to consult
     * @param externalService the actual external service to delegate to
     */
    public ExternalResourceProxy(RateLimiterService rateLimiter, ExternalService externalService) {
        this.rateLimiter = rateLimiter;
        this.externalService = externalService;
    }

    /**
     * Attempts to call the external resource, subject to rate limiting.
     *
     * @param key     the rate-limiting key (e.g., "tenant:T1")
     * @param request the request payload
     * @return the response from the external service
     * @throws RateLimitExceededException if the rate limit is exceeded
     */
    public String callExternalResource(String key, String request) {
        if (!rateLimiter.allowRequest(key)) {
            throw new RateLimitExceededException(key);
        }
        return externalService.call(request);
    }
}
