import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Fixed Window Counter rate limiting algorithm.
 *
 * Divides time into fixed-size windows (e.g., every minute boundary).
 * Each key gets an independent counter that resets when a new window starts.
 *
 * ── Trade-offs ──
 * ✅ Simple and memory-efficient (one counter per key)
 * ✅ Lock-free via AtomicInteger — high throughput under contention
 * ❌ Boundary burst problem: up to 2× the limit can pass at window edges
 *    (e.g., 5 at 0:59 + 5 at 1:01 = 10 in 2 seconds)
 *
 * Thread Safety: ConcurrentHashMap + AtomicInteger + AtomicLong (CAS-based)
 */
public class FixedWindowCounter implements RateLimitAlgorithm {

    private final int maxRequests;
    private final long windowSizeMs;
    private final ConcurrentHashMap<String, WindowData> windows;

    public FixedWindowCounter(RateLimitConfig config) {
        this.maxRequests = config.getMaxRequests();
        this.windowSizeMs = config.getWindowSizeMs();
        this.windows = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allowRequest(String key) {
        long now = System.currentTimeMillis();
        long currentWindowStart = (now / windowSizeMs) * windowSizeMs;

        // Get or create a WindowData for this key
        WindowData data = windows.compute(key, (k, existing) -> {
            if (existing == null || existing.getWindowStart() != currentWindowStart) {
                // New window — reset counter
                return new WindowData(currentWindowStart);
            }
            return existing;
        });

        // Atomically increment and check against the limit
        int currentCount = data.getCounter().incrementAndGet();
        return currentCount <= maxRequests;
    }

    // ── Inner class ─────────────────────────────────────────────

    /**
     * Holds per-key data for a single fixed window.
     */
    private static class WindowData {
        private final long windowStart;
        private final AtomicInteger counter;

        WindowData(long windowStart) {
            this.windowStart = windowStart;
            this.counter = new AtomicInteger(0);
        }

        long getWindowStart() {
            return windowStart;
        }

        AtomicInteger getCounter() {
            return counter;
        }
    }
}
