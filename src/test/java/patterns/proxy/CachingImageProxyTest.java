package patterns.proxy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CachingImageProxyTest {
    @Test
    void secondLoadOfTheSameImageHitsTheCache() {
        RealImageService real = new RealImageService();
        ImageService proxy = new CachingImageProxy(real);
        proxy.load("a");
        proxy.load("a");
        assertEquals(1, real.getLoads());
    }

    @Test
    void differentImagesAreLoadedSeparately() {
        RealImageService real = new RealImageService();
        ImageService proxy = new CachingImageProxy(real);
        proxy.load("a");
        proxy.load("b");
        assertEquals(2, real.getLoads());
    }

    @Test
    void returnsTheSameResultAsTheRealService() {
        ImageService proxy = new CachingImageProxy(new RealImageService());
        assertEquals("pixels-of-a", proxy.load("a"));
    }
}
