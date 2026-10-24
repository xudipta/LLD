package patterns.proxy;

import java.util.HashMap;
import java.util.Map;

/** Caching proxy: same interface as the real service, so callers cannot tell them apart. */
public class CachingImageProxy implements ImageService {
    private final ImageService target;
    private final Map<String, String> cache = new HashMap<>();

    public CachingImageProxy(ImageService target) {
        this.target = target;
    }

    @Override
    public String load(String name) {
        return cache.computeIfAbsent(name, target::load);
    }
}
