package problems.lrucache;

public interface Cache<K, V> {
    /** @return the value, or null if absent. A hit makes the key most recently used. */
    V get(K key);

    void put(K key, V value);

    int size();
}
