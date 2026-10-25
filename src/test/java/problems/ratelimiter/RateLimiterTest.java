package problems.ratelimiter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class RateLimiterTest {
    private static final long SECOND = 1_000_000_000L;

    // ---- behaviour, with a fake clock ----
    @Test
    void allowsUpToCapacityThenRejects() {
        AtomicLong now = new AtomicLong();
        RateLimiter r = new TokenBucketRateLimiter(3, 1, now::get);
        assertTrue(r.tryAcquire("k"));
        assertTrue(r.tryAcquire("k"));
        assertTrue(r.tryAcquire("k"));
        assertFalse(r.tryAcquire("k"));
    }

    @Test
    void refillsOverTime() {
        AtomicLong now = new AtomicLong();
        RateLimiter r = new TokenBucketRateLimiter(2, 1, now::get);
        r.tryAcquire("k");
        r.tryAcquire("k");
        assertFalse(r.tryAcquire("k"));
        now.addAndGet(SECOND);
        assertTrue(r.tryAcquire("k"));
        assertFalse(r.tryAcquire("k"));
    }

    @Test
    void partialRefillIsNotEnough() {
        AtomicLong now = new AtomicLong();
        RateLimiter r = new TokenBucketRateLimiter(1, 1, now::get);
        r.tryAcquire("k");
        now.addAndGet(SECOND / 2);
        assertFalse(r.tryAcquire("k"));
        now.addAndGet(SECOND / 2);
        assertTrue(r.tryAcquire("k"));
    }

    @Test
    void neverRefillsPastCapacity() {
        AtomicLong now = new AtomicLong();
        RateLimiter r = new TokenBucketRateLimiter(2, 1, now::get);
        now.addAndGet(100 * SECOND);
        assertTrue(r.tryAcquire("k"));
        assertTrue(r.tryAcquire("k"));
        assertFalse(r.tryAcquire("k"));
    }

    @Test
    void keysAreIndependent() {
        AtomicLong now = new AtomicLong();
        RateLimiter r = new TokenBucketRateLimiter(1, 0, now::get);
        assertTrue(r.tryAcquire("a"));
        assertFalse(r.tryAcquire("a"));
        assertTrue(r.tryAcquire("b"));
    }

    @Test
    void rejectsInvalidConfiguration() {
        assertThrows(IllegalArgumentException.class, () -> new TokenBucketRateLimiter(0, 1));
        assertThrows(IllegalArgumentException.class, () -> new TokenBucketRateLimiter(1, -1));
    }

    // ---- concurrency ----
    private int allowedUnderContention(RateLimiter limiter, int threads, int attemptsPerThread) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger allowed = new AtomicInteger();
        List<Future<?>> futures = new ArrayList<>();
        for (int t = 0; t < threads; t++) {
            futures.add(pool.submit(() -> {
                start.await();
                for (int i = 0; i < attemptsPerThread; i++) {
                    if (limiter.tryAcquire("shared")) allowed.incrementAndGet();
                }
                return null;
            }));
        }
        start.countDown();
        for (Future<?> f : futures) f.get(30, TimeUnit.SECONDS);
        pool.shutdown();
        return allowed.get();
    }

    @Test
    void tokenBucketNeverExceedsTheLimitUnderContention() throws Exception {
        RateLimiter r = new TokenBucketRateLimiter(100, 0, () -> 0L);
        assertEquals(100, allowedUnderContention(r, 8, 200));
    }

    @Test
    void naiveLimiterOverAdmitsUnderContention() throws Exception {
        int allowed = 0;
        for (int attempt = 0; attempt < 5 && allowed <= 50; attempt++) {
            allowed = allowedUnderContention(new NaiveTokenBucketRateLimiter(50), 8, 100);
        }
        assumeTrue(allowed > 50, "race not reproduced on this machine/run");
        assertTrue(allowed > 50);
    }
}
