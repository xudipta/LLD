package patterns.state;

public interface OrderState {
    OrderState pay();

    OrderState ship();

    OrderState deliver();

    String name();
}
