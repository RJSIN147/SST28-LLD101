/**
 * Factory interface for creating eviction policy instances.
 *
 * Each cache node needs its own independent eviction policy instance.
 * This factory ensures that new policies are created per node rather
 * than shared (which would cause incorrect eviction behavior).
 *
 * @param <K> the type of cache keys
 */
public interface EvictionPolicyFactory<K> {

    /**
     * Creates a new eviction policy instance.
     *
     * @return a fresh eviction policy
     */
    EvictionPolicy<K> create();
}
