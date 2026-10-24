package patterns.proxy;

public class EntryPoint {
    public static void main(String[] args) {
        RealImageService real = new RealImageService();
        ImageService service = new CachingImageProxy(real);
        service.load("logo.png");
        service.load("logo.png");
        service.load("banner.png");
        System.out.println("Real loads: " + real.getLoads());
    }
}
