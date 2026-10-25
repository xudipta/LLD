package problems.lrucache;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe with one lock. A read/write lock would not help: get() mutates the
 * recency list, so every operation needs exclusive access.
 */
public class ConcurrentLruCache<K, V> implements Cache<K, V> {
    private final LruCore<K, V> core;
    private final ReentrantLock lock = new ReentrantLock();

    public ConcurrentLruCache(int capacity) {
        core = new LruCore<>(capacity);
    }

    @Override
    public V get(K key) {
        lock.lock();
        try {
            return core.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        lock.lock();
        try {
            core.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return core.size();
        } finally {
            lock.unlock();
        }
    }

    boolean isConsistent() {
        lock.lock();
        try {
            return core.isConsistent();
        } finally {
            lock.unlock();
        }
    }
}
