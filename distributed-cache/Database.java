/**
 * Interface representing the backing data store.
 *
 * On a cache miss, the cache system fetches data from the database
 * and populates the cache before returning the value.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public interface Database<K, V> {

    /**
     * Retrieves a value from the database by key.
     *
     * @param key the key to look up
     * @return the value, or null if not found
     */
    V get(K key);

    /**
     * Stores a key-value pair in the database.
     *
     * @param key   the key
     * @param value the value
     */
    void put(K key, V value);
}
