package factory_design_pattern;

public class ShapeFactory {

    public Shape getShape(String str){
        return switch (str) {
            case "circle" -> new Circle();
            case "square" -> new Square();
            default -> null;
        };
    }
}
