/**
 * Demo driver — demonstrates the full rate limiting system.
 *
 * Shows:
 *   1. Fixed Window Counter in action (5 requests/minute for tenant T1)
 *   2. Runtime algorithm swap to Sliding Window Counter (no business logic change)
 *   3. Multi-tenant isolation (T1 and T2 have independent quotas)
 *   4. Business logic deciding when to call the external resource
 */
public class App {

    public static void main(String[] args) throws InterruptedException {

        // ── Configuration ───────────────────────────────────────
        RateLimitConfig config = RateLimitConfig.perMinute(5);
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     PLUGGABLE RATE LIMITER — DEMO               ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("Config: " + config);
        System.out.println();

        // ── Setup with Fixed Window Counter ─────────────────────
        RateLimitAlgorithm fixedWindow = new FixedWindowCounter(config);
        RateLimiterService rateLimiter = new RateLimiterService(fixedWindow);

        ExternalService paidApi = new PaidApiService("GeocodingAPI");
        ExternalResourceProxy proxy = new ExternalResourceProxy(rateLimiter, paidApi);

        // ── Demo 1: Fixed Window Counter ────────────────────────
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("DEMO 1: Fixed Window Counter — Tenant T1 (5 req/min)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        simulateRequests(proxy, "tenant:T1", 8);

        // ── Demo 2: Runtime Algorithm Swap ──────────────────────
        System.out.println();
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("DEMO 2: Swapping to Sliding Window Counter at runtime");
        System.out.println("        (No changes to business logic / proxy code!)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        RateLimitAlgorithm slidingWindow = new SlidingWindowCounter(config);
        rateLimiter.setAlgorithm(slidingWindow);  // ← one-line swap
        System.out.println("Algorithm swapped to: " + slidingWindow.getClass().getSimpleName());
        System.out.println();

        simulateRequests(proxy, "tenant:T1", 8);

        // ── Demo 3: Multi-Tenant Isolation ──────────────────────
        System.out.println();
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("DEMO 3: Multi-Tenant Isolation (T1 vs T2)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Fresh algorithm so counters are clean
        rateLimiter.setAlgorithm(new FixedWindowCounter(config));
        System.out.println("Using: FixedWindowCounter (fresh counters)\n");

        System.out.println("── Tenant T1 ──");
        simulateRequests(proxy, "tenant:T1", 4);
        System.out.println();
        System.out.println("── Tenant T2 ──");
        simulateRequests(proxy, "tenant:T2", 4);
        System.out.println();
        System.out.println("── Tenant T1 (continued — already used 4 of 5) ──");
        simulateRequests(proxy, "tenant:T1", 3);

        // ── Demo 4: Business Logic — Not Every Request Hits Limiter ─
        System.out.println();
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("DEMO 4: Business Logic — External Call Only When Needed");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        rateLimiter.setAlgorithm(new FixedWindowCounter(config));
        simulateBusinessLogic(proxy);

        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     DEMO COMPLETE                                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    /**
     * Sends N requests through the proxy for the given key.
     */
    private static void simulateRequests(ExternalResourceProxy proxy, String key, int count) {
        for (int i = 1; i <= count; i++) {
            try {
                String response = proxy.callExternalResource(key, "request-" + i);
                System.out.println("  Request " + i + " → ✅ ALLOWED | " + response);
            } catch (RateLimitExceededException e) {
                System.out.println("  Request " + i + " → ❌ DENIED  | " + e.getMessage());
            }
        }
    }

    /**
     * Simulates business logic where only some requests need external calls.
     */
    private static void simulateBusinessLogic(ExternalResourceProxy proxy) {
        String key = "tenant:T1";

        for (int i = 1; i <= 10; i++) {
            boolean needsExternalCall = (i % 2 == 0); // Only even requests need external call

            if (!needsExternalCall) {
                System.out.println("  Request " + i + " → 🔹 No external call needed (handled internally)");
            } else {
                try {
                    String response = proxy.callExternalResource(key, "biz-request-" + i);
                    System.out.println("  Request " + i + " → ✅ ALLOWED | " + response);
                } catch (RateLimitExceededException e) {
                    System.out.println("  Request " + i + " → ❌ DENIED  | " + e.getMessage());
                }
            }
        }
    }
}
