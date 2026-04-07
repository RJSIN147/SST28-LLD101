/**
 * Immutable configuration for rate limiting.
 * 
 * Holds the maximum number of requests allowed within a given time window.
 * Instances are thread-safe as all fields are final.
 */
public class RateLimitConfig {

    private final int maxRequests;
    private final long windowSizeMs;

    /**
     * Creates a rate limit configuration.
     *
     * @param maxRequests  maximum number of requests allowed in the window
     * @param windowSizeMs window size in milliseconds
     */
    public RateLimitConfig(int maxRequests, long windowSizeMs) {
        if (maxRequests <= 0) {
            throw new IllegalArgumentException("maxRequests must be positive");
        }
        if (windowSizeMs <= 0) {
            throw new IllegalArgumentException("windowSizeMs must be positive");
        }
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
    }

    // ── Factory methods ─────────────────────────────────────────

    /**
     * Creates a config allowing {@code max} requests per minute.
     */
    public static RateLimitConfig perMinute(int max) {
        return new RateLimitConfig(max, 60_000L);
    }

    /**
     * Creates a config allowing {@code max} requests per hour.
     */
    public static RateLimitConfig perHour(int max) {
        return new RateLimitConfig(max, 3_600_000L);
    }

    // ── Getters ─────────────────────────────────────────────────

    public int getMaxRequests() {
        return maxRequests;
    }

    public long getWindowSizeMs() {
        return windowSizeMs;
    }

    @Override
    public String toString() {
        return maxRequests + " requests per " + windowSizeMs + "ms";
    }
}
