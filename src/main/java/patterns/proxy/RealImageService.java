package patterns.proxy;

public class RealImageService implements ImageService {
    private int loads;

    @Override
    public String load(String name) {
        loads++;
        return "pixels-of-" + name; // pretend this is slow
    }

    public int getLoads() { return loads; }
}
