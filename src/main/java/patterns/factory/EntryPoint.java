package patterns.factory;

public class EntryPoint {
    public static void main(String[] args) {
        ShapeFactory factory = new ShapeFactory();
        Shape shape = factory.getShape("circle");
        shape.draw();
        shape = factory.getShape("square");
        shape.draw();
    }
}
