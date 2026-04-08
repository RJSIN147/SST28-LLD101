import java.util.HashMap;
import java.util.Map;

/**
 * Simulated in-memory database for demo/testing purposes.
 *
 * In a real system, this would be replaced by a JDBC-backed implementation,
 * a NoSQL client, or any persistent data store.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class InMemoryDatabase<K, V> implements Database<K, V> {

    private final Map<K, V> store;

    public InMemoryDatabase() {
        this.store = new HashMap<>();
    }

    @Override
    public V get(K key) {
        return store.get(key);
    }

    @Override
    public void put(K key, V value) {
        store.put(key, value);
    }

    /**
     * Pre-loads data into the database (for demo/testing).
     *
     * @param data the data to load
     */
    public void loadData(Map<K, V> data) {
        store.putAll(data);
    }

    @Override
    public String toString() {
        return "InMemoryDatabase{size=" + store.size() + "}";
    }
}
