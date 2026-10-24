package patterns.state;

class PaidState extends AbstractOrderState {
    @Override
    public OrderState ship() { return new ShippedState(); }

    @Override
    public String name() { return "PAID"; }
}
