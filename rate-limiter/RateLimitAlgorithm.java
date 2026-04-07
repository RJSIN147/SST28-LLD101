/**
 * Strategy interface for rate limiting algorithms.
 * 
 * Each implementation defines its own logic for deciding whether
 * a request identified by a key should be allowed or denied.
 * 
 * The key can represent any dimension: tenant ID, customer ID,
 * API key, external provider name, etc.
 */
public interface RateLimitAlgorithm {

    /**
     * Determines whether a request for the given key is allowed.
     *
     * @param key the rate-limiting key (e.g., "tenant:T1", "customer:42")
     * @return true if the request is within the allowed limit, false otherwise
     */
    boolean allowRequest(String key);
}
