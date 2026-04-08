import java.util.ArrayList;
import java.util.List;

/**
 * Distributed cache that spreads data across multiple {@link CacheNode}s.
 *
 * ── How data is distributed ──
 * A pluggable {@link DistributionStrategy} maps each key to a node index.
 * The default implementation uses modulo hashing: hash(key) % numberOfNodes.
 *
 * ── How cache miss is handled ──
 * When get(key) results in a cache miss at the assigned node:
 *   1. The value is fetched from the backing {@link Database}
 *   2. The value is stored in the appropriate cache node (populating the cache)
 *   3. The value is returned to the caller
 * If the key is also not in the database, null is returned.
 *
 * ── How eviction works ──
 * Each cache node has a fixed capacity and a pluggable {@link EvictionPolicy}.
 * When a node is full and a new key needs to be inserted, the eviction policy
 * (e.g., LRU) decides which existing entry to remove.
 *
 * ── Extensibility ──
 * - Swap DistributionStrategy (e.g., to Consistent Hashing) without changing cache logic
 * - Swap EvictionPolicy (e.g., to LFU, MRU) without changing cache or distribution logic
 * - Add more nodes by changing the configuration
 *
 * @param <K> the type of cache keys
 * @param <V> the type of cache values
 */
public class DistributedCache<K, V> {

    private final List<CacheNode<K, V>> nodes;
    private final DistributionStrategy<K> distributionStrategy;
    private final Database<K, V> database;

    /**
     * Creates a distributed cache.
     *
     * @param numberOfNodes        number of cache nodes to create
     * @param capacityPerNode      max entries per node
     * @param distributionStrategy strategy for mapping keys to nodes
     * @param evictionPolicyFactory factory to create a fresh eviction policy per node
     * @param database             the backing data store
     */
    public DistributedCache(int numberOfNodes,
                            int capacityPerNode,
                            DistributionStrategy<K> distributionStrategy,
                            EvictionPolicyFactory<K> evictionPolicyFactory,
                            Database<K, V> database) {
        if (numberOfNodes <= 0) {
            throw new IllegalArgumentException("numberOfNodes must be positive");
        }

        this.distributionStrategy = distributionStrategy;
        this.database = database;
        this.nodes = new ArrayList<>();

        for (int i = 0; i < numberOfNodes; i++) {
            EvictionPolicy<K> policy = evictionPolicyFactory.create();
            nodes.add(new CacheNode<>(i, capacityPerNode, policy));
        }
    }

    /**
     * Retrieves a value by key.
     *
     * Flow:
     *   1. Determine which node owns this key (via distribution strategy)
     *   2. If key is in cache → return (cache hit)
     *   3. If key is NOT in cache → fetch from database, populate cache, return
     *
     * @param key the key to look up
     * @return the value, or null if not found in cache or database
     */
    public V get(K key) {
        int nodeIndex = distributionStrategy.getNodeIndex(key, nodes.size());
        CacheNode<K, V> node = nodes.get(nodeIndex);

        // Try cache first
        V value = node.get(key);
        if (value != null) {
            System.out.println("  [Cache HIT]  key=" + key + " → Node " + nodeIndex);
            return value;
        }

        // Cache miss — fetch from database
        System.out.println("  [Cache MISS] key=" + key + " → fetching from DB, storing in Node " + nodeIndex);
        value = database.get(key);
        if (value != null) {
            node.put(key, value);
        }
        return value;
    }

    /**
     * Stores a key-value pair.
     *
     * Flow:
     *   1. Determine which node owns this key
     *   2. Store in the cache node (eviction happens automatically if full)
     *   3. Also update the database
     *
     * @param key   the key
     * @param value the value
     */
    public void put(K key, V value) {
        int nodeIndex = distributionStrategy.getNodeIndex(key, nodes.size());
        CacheNode<K, V> node = nodes.get(nodeIndex);

        System.out.println("  [PUT]        key=" + key + " → Node " + nodeIndex);
        node.put(key, value);
        database.put(key, value); // Write-through to database
    }

    /**
     * Returns the list of cache nodes (for diagnostics/testing).
     */
    public List<CacheNode<K, V>> getNodes() {
        return nodes;
    }

    /**
     * Prints the status of all cache nodes.
     */
    public void printStatus() {
        System.out.println("  Cache Status:");
        for (CacheNode<K, V> node : nodes) {
            System.out.println("    " + node);
        }
    }
}
