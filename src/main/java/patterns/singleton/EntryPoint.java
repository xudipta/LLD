package patterns.singleton;

public class EntryPoint {
    public static void main(String[] args) {
        AppConfig.getInstance().set("env", "prod");
        System.out.println("env = " + AppConfig.getInstance().get("env"));
        System.out.println("Same instance: " + (AppConfig.getInstance() == AppConfig.getInstance()));
    }
}
