package patterns.abstractfactory;
import patterns.abstractfactory.factory.GUIFactory;
import patterns.abstractfactory.factory.MacFactory;
import patterns.abstractfactory.factory.WinFactory;
import patterns.abstractfactory.product.Application;

public class EntryPoint {
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
