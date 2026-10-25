package problems.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Deliberately NOT thread-safe, and with no refill so the limit is easy to state:
 * at most {@code capacity} calls per key, ever. The map is concurrent, but the
 * check-then-act on the counter is not, so concurrent callers can all pass the
 * check before any of them decrements.
 */
public class NaiveTokenBucketRateLimiter implements RateLimiter {
    private static final class Bucket {
        int tokens;

        Bucket(int tokens) {
            this.tokens = tokens;
        }
    }

    private final int capacity;
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public NaiveTokenBucketRateLimiter(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean tryAcquire(String key) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> new Bucket(capacity));
        if (bucket.tokens > 0) {      // check
            Thread.yield();           // widens the window so the race shows up reliably
            bucket.tokens--;          // act
            return true;
        }
        return false;
    }
}
