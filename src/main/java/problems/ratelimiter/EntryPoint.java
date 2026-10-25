package problems.ratelimiter;

public class EntryPoint {
    public static void main(String[] args) {
        RateLimiter limiter = new TokenBucketRateLimiter(3, 1);
        for (int i = 1; i <= 5; i++) {
            System.out.println("request " + i + ": " + (limiter.tryAcquire("user-1") ? "allowed" : "rejected"));
        }
    }
}
