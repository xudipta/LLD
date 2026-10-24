package patterns.singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Initialization-on-demand holder idiom: lazy, thread-safe, no explicit locking.
 * The JVM guarantees the nested class is initialised once, on first use.
 */
public final class AppConfig {
    private final Map<String, String> values = new ConcurrentHashMap<>();

    private AppConfig() {
    }

    private static final class Holder {
        static final AppConfig INSTANCE = new AppConfig();
    }

    public static AppConfig getInstance() {
        return Holder.INSTANCE;
    }

    public void set(String key, String value) {
        values.put(key, value);
    }

    public String get(String key) {
        return values.get(key);
    }
}
