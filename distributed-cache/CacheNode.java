import java.util.HashMap;
import java.util.Map;

/**
 * A single cache node with a fixed capacity and a pluggable eviction policy.
 *
 * Each node is an independent key-value store. When the capacity is full
 * and a new key needs to be inserted, the eviction policy decides which
 * existing key to remove.
 *
 * ── Thread Safety ──
 * All public methods are synchronized, making each node safe for
 * concurrent access from multiple threads.
 *
 * @param <K> the type of cache keys
 * @param <V> the type of cache values
 */
public class CacheNode<K, V> {

    private final int id;
    private final int capacity;
    private final Map<K, V> store;
    private final EvictionPolicy<K> evictionPolicy;

    /**
     * Creates a cache node.
     *
     * @param id             unique identifier for this node
     * @param capacity       maximum number of entries
     * @param evictionPolicy the eviction policy to use when full
     */
    public CacheNode(int id, int capacity, EvictionPolicy<K> evictionPolicy) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.id = id;
        this.capacity = capacity;
        this.store = new HashMap<>();
        this.evictionPolicy = evictionPolicy;
    }

    /**
     * Gets a value from this cache node.
     *
     * @param key the key to look up
     * @return the cached value, or null if not present in this node
     */
    public synchronized V get(K key) {
        if (!store.containsKey(key)) {
            return null; // Cache miss at this node
        }
        evictionPolicy.keyAccessed(key);
        return store.get(key);
    }

    /**
     * Puts a key-value pair into this cache node.
     * If the node is at capacity, the eviction policy determines which key to remove.
     *
     * @param key   the key
     * @param value the value
     */
    public synchronized void put(K key, V value) {
        if (store.containsKey(key)) {
            // Update existing key
            store.put(key, value);
            evictionPolicy.keyAccessed(key);
            return;
        }

        // Evict if at capacity
        if (store.size() >= capacity) {
            K evictedKey = evictionPolicy.evictKey();
            store.remove(evictedKey);
            System.out.println("    [Node " + id + "] Evicted key: " + evictedKey);
        }

        store.put(key, value);
        evictionPolicy.keyAccessed(key);
    }

    /**
     * Returns the current number of entries in this node.
     */
    public synchronized int size() {
        return store.size();
    }

    /**
     * Returns the capacity of this node.
     */
    public int getCapacity() {
        return capacity;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "CacheNode{id=" + id + ", size=" + store.size() + "/" + capacity + "}";
    }
}
