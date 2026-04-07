/**
 * Facade service that internal services use to check rate limits
 * before making external resource calls.
 *
 * Delegates to the currently configured {@link RateLimitAlgorithm}.
 * The algorithm can be swapped at runtime via {@link #setAlgorithm(RateLimitAlgorithm)}
 * without changing any caller code — this is the key benefit of the Strategy pattern.
 *
 * Thread Safety: The algorithm reference is volatile, ensuring visibility
 * across threads when the algorithm is swapped at runtime.
 */
public class RateLimiterService {

    private volatile RateLimitAlgorithm algorithm;

    /**
     * Creates a rate limiter service with the given algorithm.
     *
     * @param algorithm the rate limiting algorithm to use
     */
    public RateLimiterService(RateLimitAlgorithm algorithm) {
        if (algorithm == null) {
            throw new IllegalArgumentException("algorithm must not be null");
        }
        this.algorithm = algorithm;
    }

    /**
     * Checks whether a request for the given key is allowed.
     *
     * @param key the rate-limiting key
     * @return true if allowed, false if rate limit is exceeded
     */
    public boolean allowRequest(String key) {
        return algorithm.allowRequest(key);
    }

    /**
     * Swaps the rate limiting algorithm at runtime.
     * Callers continue using {@link #allowRequest(String)} without any changes.
     *
     * @param algorithm the new algorithm to use
     */
    public void setAlgorithm(RateLimitAlgorithm algorithm) {
        if (algorithm == null) {
            throw new IllegalArgumentException("algorithm must not be null");
        }
        this.algorithm = algorithm;
    }

    /**
     * Returns the currently active algorithm (for diagnostics/logging).
     */
    public RateLimitAlgorithm getAlgorithm() {
        return algorithm;
    }
}
