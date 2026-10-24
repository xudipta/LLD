package patterns.state;

/** Every transition is illegal unless a concrete state overrides it. */
abstract class AbstractOrderState implements OrderState {
    @Override
    public OrderState pay() {
        throw illegal("pay");
    }

    @Override
    public OrderState ship() {
        throw illegal("ship");
    }

    @Override
    public OrderState deliver() {
        throw illegal("deliver");
    }

    private IllegalStateException illegal(String action) {
        return new IllegalStateException("Cannot " + action + " an order that is " + name());
    }
}
