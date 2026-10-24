package patterns.state;

class NewState extends AbstractOrderState {
    @Override
    public OrderState pay() { return new PaidState(); }

    @Override
    public String name() { return "NEW"; }
}
