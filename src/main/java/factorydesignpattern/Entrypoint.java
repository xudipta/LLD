package factorydesignpattern;

public class Entrypoint {
    public static void main(String[] args) {
        ShapeFactory factory = new ShapeFactory();
        Shape shape = factory.getShape("circle");
        shape.draw();
        shape = factory.getShape("square");
        shape.draw();
    }
}
