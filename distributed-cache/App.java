import java.util.HashMap;
import java.util.Map;

/**
 * Demo driver — demonstrates the full distributed cache system.
 *
 * Shows:
 *   1. Data distribution across nodes (modulo hashing)
 *   2. Cache miss → DB fetch → cache populate
 *   3. Cache hit (subsequent access)
 *   4. LRU eviction when a node reaches capacity
 *   5. Write-through on put()
 */
public class App {

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     DISTRIBUTED CACHE — DEMO                    ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        // ── Setup Database ──────────────────────────────────────
        InMemoryDatabase<String, String> database = new InMemoryDatabase<>();
        Map<String, String> seedData = new HashMap<>();
        seedData.put("user:1", "Alice");
        seedData.put("user:2", "Bob");
        seedData.put("user:3", "Charlie");
        seedData.put("user:4", "Diana");
        seedData.put("user:5", "Eve");
        seedData.put("user:6", "Frank");
        seedData.put("user:7", "Grace");
        seedData.put("user:8", "Heidi");
        database.loadData(seedData);
        System.out.println("Database seeded with " + seedData.size() + " entries");
        System.out.println();

        // ── Setup Distributed Cache ─────────────────────────────
        // 3 nodes, 3 entries per node, modulo distribution, LRU eviction
        int numberOfNodes = 3;
        int capacityPerNode = 3;

        DistributedCache<String, String> cache = new DistributedCache<>(
                numberOfNodes,
                capacityPerNode,
                new ModuloDistributionStrategy<>(),  // Pluggable — swap to ConsistentHashing later
                LRUEvictionPolicy::new,              // Pluggable — swap to MRU/LFU later
                database
        );

        System.out.println("Cache created: " + numberOfNodes + " nodes × " + capacityPerNode + " capacity each");
        System.out.println("Distribution: ModuloDistributionStrategy");
        System.out.println("Eviction:     LRU (Least Recently Used)");
        System.out.println();

        // ── Demo 1: Cache Misses → DB Fetch → Populate ─────────
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("DEMO 1: Cache Misses (first access fetches from DB)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        String[] keys = {"user:1", "user:2", "user:3", "user:4", "user:5"};
        for (String key : keys) {
            String value = cache.get(key);
            System.out.println("    → got: " + value);
        }
        System.out.println();
        cache.printStatus();
        System.out.println();

        // ── Demo 2: Cache Hits (second access) ─────────────────
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("DEMO 2: Cache Hits (data already in cache)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        for (String key : new String[]{"user:1", "user:3", "user:5"}) {
            String value = cache.get(key);
            System.out.println("    → got: " + value);
        }
        System.out.println();

        // ── Demo 3: LRU Eviction ────────────────────────────────
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("DEMO 3: LRU Eviction (adding more keys than capacity)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        System.out.println("Adding keys user:6, user:7, user:8 — some nodes will evict LRU entries");
        System.out.println();
        for (String key : new String[]{"user:6", "user:7", "user:8"}) {
            String value = cache.get(key);
            System.out.println("    → got: " + value);
        }
        System.out.println();
        cache.printStatus();
        System.out.println();

        // ── Demo 4: put() — Write-through ───────────────────────
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("DEMO 4: put() — Write-through to cache + database");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        cache.put("user:9", "Ivan");
        String fetched = cache.get("user:9");
        System.out.println("    → get after put: " + fetched);
        System.out.println();

        // ── Demo 5: Verify evicted key re-fetches from DB ───────
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("DEMO 5: Re-fetching an evicted key (cache miss → DB)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        System.out.println("Fetching user:1 again (may have been evicted):");
        String refetched = cache.get("user:1");
        System.out.println("    → got: " + refetched);
        System.out.println();

        cache.printStatus();
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     DEMO COMPLETE                                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }
}
