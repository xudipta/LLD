package implementations.lambda;

/***
 * Functional interface can have only one abstract method
 * default / static method can be present
 * ****/
@FunctionalInterface
public interface Printable {
    void print();
}
