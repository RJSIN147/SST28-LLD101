/**
 * Strategy interface for cache eviction policies.
 *
 * Defines how a cache node decides which entry to evict when capacity is full,
 * and how it tracks access patterns for eviction decisions.
 *
 * Extensibility: Implement this interface to add new policies such as
 * MRU (Most Recently Used), LFU (Least Frequently Used), FIFO, etc.
 *
 * @param <K> the type of cache keys
 */
public interface EvictionPolicy<K> {

    /**
     * Called when a key is accessed (get or put).
     * Implementations should update internal tracking structures.
     *
     * @param key the key that was accessed
     */
    void keyAccessed(K key);

    /**
     * Returns the key that should be evicted according to this policy.
     *
     * @return the key to evict
     * @throws java.util.NoSuchElementException if there are no keys to evict
     */
    K evictKey();

    /**
     * Called when a key is explicitly removed from the cache.
     *
     * @param key the key being removed
     */
    void keyRemoved(K key);
}
