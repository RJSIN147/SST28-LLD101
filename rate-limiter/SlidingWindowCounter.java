import java.util.concurrent.ConcurrentHashMap;

/**
 * Sliding Window Counter rate limiting algorithm.
 *
 * A hybrid approach that smooths out the boundary burst problem of fixed windows
 * by combining the current window's count with a weighted portion of the previous
 * window's count.
 *
 * Formula:
 *   effectiveCount = previousWindowCount × overlapRatio + currentWindowCount
 *
 * where overlapRatio = (windowSizeMs - elapsedInCurrentWindow) / windowSizeMs
 *
 * ── Trade-offs ──
 * ✅ Significantly reduces boundary burst problem
 * ✅ Still memory-efficient (two counters per key)
 * ✅ Better accuracy than fixed window, lower overhead than sliding log
 * ❌ Slightly more complex than fixed window
 * ❌ Requires synchronized blocks for compound read-check-update
 * ❌ Approximation — not an exact sliding window (but good enough for most use cases)
 *
 * Thread Safety: ConcurrentHashMap + synchronized on per-key SlidingWindowData
 */
public class SlidingWindowCounter implements RateLimitAlgorithm {

    private final int maxRequests;
    private final long windowSizeMs;
    private final ConcurrentHashMap<String, SlidingWindowData> windows;

    public SlidingWindowCounter(RateLimitConfig config) {
        this.maxRequests = config.getMaxRequests();
        this.windowSizeMs = config.getWindowSizeMs();
        this.windows = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allowRequest(String key) {
        long now = System.currentTimeMillis();
        long currentWindowStart = (now / windowSizeMs) * windowSizeMs;

        // Get or create per-key data
        SlidingWindowData data = windows.computeIfAbsent(key,
                k -> new SlidingWindowData(currentWindowStart));

        synchronized (data) {
            // If we've moved past the current window, rotate
            if (currentWindowStart != data.currentWindowStart) {
                if (currentWindowStart - data.currentWindowStart == windowSizeMs) {
                    // Moved exactly one window forward — current becomes previous
                    data.previousCount = data.currentCount;
                } else {
                    // Moved more than one window — previous data is stale
                    data.previousCount = 0;
                }
                data.currentCount = 0;
                data.currentWindowStart = currentWindowStart;
            }

            // Calculate the weighted effective count
            long elapsedInCurrentWindow = now - currentWindowStart;
            double overlapRatio = (windowSizeMs - elapsedInCurrentWindow) / (double) windowSizeMs;
            double effectiveCount = data.previousCount * overlapRatio + data.currentCount;

            if (effectiveCount + 1 > maxRequests) {
                return false; // Deny — limit would be exceeded
            }

            data.currentCount++;
            return true; // Allow
        }
    }

    // ── Inner class ─────────────────────────────────────────────

    /**
     * Holds per-key data for the sliding window counter.
     * Access must be synchronized externally.
     */
    private static class SlidingWindowData {
        long currentWindowStart;
        int currentCount;
        int previousCount;

        SlidingWindowData(long currentWindowStart) {
            this.currentWindowStart = currentWindowStart;
            this.currentCount = 0;
            this.previousCount = 0;
        }
    }
}
