package abstract_factory_design_pattern;
import abstract_factory_design_pattern.factory.GUIFactory;
import abstract_factory_design_pattern.factory.MacFactory;
import abstract_factory_design_pattern.factory.WinFactory;
import abstract_factory_design_pattern.product.Application;

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
