package problems.ratelimiter;

public interface RateLimiter {
    /** @return true if the call for this key is allowed right now. */
    boolean tryAcquire(String key);
}
