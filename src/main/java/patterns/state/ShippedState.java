package patterns.state;

class ShippedState extends AbstractOrderState {
    @Override
    public OrderState deliver() { return new DeliveredState(); }

    @Override
    public String name() { return "SHIPPED"; }
}
