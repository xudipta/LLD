package problems.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

/**
 * Per-key token bucket. Each bucket holds up to {@code capacity} tokens and refills
 * continuously at {@code refillPerSecond}. The clock is injected so tests are exact.
 */
public class TokenBucketRateLimiter implements RateLimiter {
    private static final double NANOS_PER_SECOND = 1_000_000_000.0;

    private static final class Bucket {
        double tokens;
        long lastRefillNanos;

        Bucket(double tokens, long now) {
            this.tokens = tokens;
            this.lastRefillNanos = now;
        }
    }

    private final int capacity;
    private final double refillPerSecond;
    private final LongSupplier nanoClock;
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(int capacity, double refillPerSecond, LongSupplier nanoClock) {
        if (capacity <= 0 || refillPerSecond < 0) {
            throw new IllegalArgumentException("capacity must be positive and refill non-negative");
        }
        this.capacity = capacity;
        this.refillPerSecond = refillPerSecond;
        this.nanoClock = nanoClock;
    }

    public TokenBucketRateLimiter(int capacity, double refillPerSecond) {
        this(capacity, refillPerSecond, System::nanoTime);
    }

    @Override
    public boolean tryAcquire(String key) {
        long now = nanoClock.getAsLong();
        Bucket bucket = buckets.computeIfAbsent(key, k -> new Bucket(capacity, now));
        // The whole read-refill-decide-update sequence must be atomic per bucket.
        synchronized (bucket) {
            double elapsedSeconds = (now - bucket.lastRefillNanos) / NANOS_PER_SECOND;
            if (elapsedSeconds > 0) {
                bucket.tokens = Math.min(capacity, bucket.tokens + elapsedSeconds * refillPerSecond);
                bucket.lastRefillNanos = now;
            }
            if (bucket.tokens >= 1) {
                bucket.tokens -= 1;
                return true;
            }
            return false;
        }
    }
}
