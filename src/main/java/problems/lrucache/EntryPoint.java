package problems.lrucache;

public class EntryPoint {
    public static void main(String[] args) {
        Cache<String, Integer> cache = new ConcurrentLruCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.get("a");      // a is now most recent
        cache.put("c", 3);   // evicts b
        System.out.println("a=" + cache.get("a") + " b=" + cache.get("b") + " c=" + cache.get("c"));
    }
}
