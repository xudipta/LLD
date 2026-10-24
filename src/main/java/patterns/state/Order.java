package patterns.state;

public class Order {
    private OrderState state = new NewState();

    public void pay() { state = state.pay(); }

    public void ship() { state = state.ship(); }

    public void deliver() { state = state.deliver(); }

    public String getStatus() { return state.name(); }
}
