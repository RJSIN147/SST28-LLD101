/**
 * Strategy interface for distributing keys across cache nodes.
 *
 * Determines which cache node (by index) should store a given key.
 *
 * Extensibility: Implement this interface to add strategies such as
 * Consistent Hashing, Range-based partitioning, etc.
 *
 * @param <K> the type of cache keys
 */
public interface DistributionStrategy<K> {

    /**
     * Returns the index of the cache node that should handle the given key.
     *
     * @param key            the cache key
     * @param numberOfNodes  the total number of cache nodes
     * @return the node index (0-based) where this key should be stored
     */
    int getNodeIndex(K key, int numberOfNodes);
}
