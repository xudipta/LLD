package patterns.singleton;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class AppConfigTest {
    @Test
    void returnsTheSameInstance() {
        assertSame(AppConfig.getInstance(), AppConfig.getInstance());
    }

    @Test
    void sharesStateAcrossCallers() {
        AppConfig.getInstance().set("k", "v");
        assertEquals("v", AppConfig.getInstance().get("k"));
    }

    @Test
    void sameInstanceUnderConcurrentAccess() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(16);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<AppConfig>> futures = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            futures.add(pool.submit(() -> {
                start.await();
                return AppConfig.getInstance();
            }));
        }
        start.countDown();
        Set<AppConfig> distinct = new HashSet<>();
        for (Future<AppConfig> f : futures) distinct.add(f.get(5, TimeUnit.SECONDS));
        pool.shutdown();
        assertEquals(1, distinct.size());
    }
}
