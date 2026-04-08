/**
 * Modulo-based distribution strategy.
 *
 * Maps a key to a node using: Math.abs(key.hashCode()) % numberOfNodes
 *
 * ── Trade-offs ──
 * ✅ Simple and fast — O(1) computation
 * ✅ Uniform distribution when hashCode() is well-distributed
 * ❌ Adding/removing nodes invalidates almost all key mappings (poor reshuffling)
 * ❌ Not suitable for dynamic cluster resizing — use Consistent Hashing instead
 *
 * @param <K> the type of cache keys
 */
public class ModuloDistributionStrategy<K> implements DistributionStrategy<K> {

    @Override
    public int getNodeIndex(K key, int numberOfNodes) {
        if (numberOfNodes <= 0) {
            throw new IllegalArgumentException("numberOfNodes must be positive");
        }
        return Math.abs(key.hashCode()) % numberOfNodes;
    }
}
