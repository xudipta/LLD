package patterns.state;

class DeliveredState extends AbstractOrderState {
    @Override
    public String name() { return "DELIVERED"; }
}
