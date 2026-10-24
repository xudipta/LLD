package abstractfactorydesignpattern;
import abstractfactorydesignpattern.factory.GUIFactory;
import abstractfactorydesignpattern.factory.MacFactory;
import abstractfactorydesignpattern.factory.WinFactory;
import abstractfactorydesignpattern.product.Application;

public class Entrypoint {
    public static void main(String[] args) {
        String os= "win";
        GUIFactory factory = switch (os.toLowerCase()) {
            case "mac" -> new MacFactory();
            case "win" -> new WinFactory();
            default -> throw new IllegalStateException("Unexpected value: " + os.toLowerCase());
        };
        Application application = new Application(factory);

        application.paint();
    }
}
