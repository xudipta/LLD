package problems.lrucache;

/**
 * Deliberately NOT thread-safe. Kept to show what goes wrong under concurrency:
 * a get() reorders the list, so even "read-only" use corrupts shared state.
 */
public class NaiveLruCache<K, V> implements Cache<K, V> {
    private final LruCore<K, V> core;

    public NaiveLruCache(int capacity) {
        core = new LruCore<>(capacity);
    }

    @Override
    public V get(K key) {
        return core.get(key);
    }

    @Override
    public void put(K key, V value) {
        core.put(key, value);
    }

    @Override
    public int size() {
        return core.size();
    }

    boolean isConsistent() {
        return core.isConsistent();
    }
}
