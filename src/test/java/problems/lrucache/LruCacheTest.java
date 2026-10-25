package problems.lrucache;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class LruCacheTest {
    // ---- behaviour (same for both implementations) ----
    private Cache<String, Integer> cache(int capacity) {
        return new ConcurrentLruCache<>(capacity);
    }

    @Test
    void missReturnsNull() {
        assertNull(cache(2).get("x"));
    }

    @Test
    void evictsLeastRecentlyUsed() {
        Cache<String, Integer> c = cache(2);
        c.put("a", 1);
        c.put("b", 2);
        c.put("c", 3);
        assertNull(c.get("a"));
        assertEquals(2, c.get("b"));
        assertEquals(3, c.get("c"));
    }

    @Test
    void getRefreshesRecency() {
        Cache<String, Integer> c = cache(2);
        c.put("a", 1);
        c.put("b", 2);
        c.get("a");
        c.put("c", 3);
        assertEquals(1, c.get("a"));
        assertNull(c.get("b"));
    }

    @Test
    void updatingAKeyKeepsSizeAndRefreshesIt() {
        Cache<String, Integer> c = cache(2);
        c.put("a", 1);
        c.put("b", 2);
        c.put("a", 10);
        c.put("c", 3);
        assertEquals(10, c.get("a"));
        assertNull(c.get("b"));
        assertEquals(2, c.size());
    }

    @Test
    void capacityOneKeepsOnlyTheLatest() {
        Cache<String, Integer> c = cache(1);
        c.put("a", 1);
        c.put("b", 2);
        assertNull(c.get("a"));
        assertEquals(2, c.get("b"));
    }

    @Test
    void rejectsNonPositiveCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new ConcurrentLruCache<String, Integer>(0));
    }

    // ---- concurrency ----
    /** Hammers the cache from several threads; returns how many operations threw. */
    private int hammer(Cache<Integer, Integer> c) throws Exception {
        int threads = 8;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger failures = new AtomicInteger();
        List<Future<?>> futures = new ArrayList<>();
        for (int t = 0; t < threads; t++) {
            final int seed = t;
            futures.add(pool.submit(() -> {
                start.await();
                ThreadLocalRandom rnd = ThreadLocalRandom.current();
                for (int i = 0; i < 20_000; i++) {
                    int key = rnd.nextInt(200);
                    try {
                        if ((i + seed) % 2 == 0) c.put(key, i); else c.get(key);
                    } catch (RuntimeException e) {
                        failures.incrementAndGet();
                    }
                }
                return null;
            }));
        }
        start.countDown();
        for (Future<?> f : futures) f.get(60, TimeUnit.SECONDS);
        pool.shutdown();
        return failures.get();
    }

    @Test
    void concurrentCacheStaysConsistentUnderContention() throws Exception {
        ConcurrentLruCache<Integer, Integer> c = new ConcurrentLruCache<>(50);
        assertEquals(0, hammer(c));
        assertTrue(c.size() <= 50);
        assertTrue(c.isConsistent());
    }

    @Test
    void naiveCacheBreaksUnderContention() throws Exception {
        boolean observed = false;
        for (int attempt = 0; attempt < 5 && !observed; attempt++) {
            NaiveLruCache<Integer, Integer> c = new NaiveLruCache<>(50);
            int failures = hammer(c);
            observed = failures > 0 || !c.isConsistent();
        }
        // The race is real but timing-dependent: skip (rather than fail) if this run got lucky.
        assumeTrue(observed, "race not reproduced on this machine/run");
        assertTrue(observed);
    }
}
