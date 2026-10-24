package patterns.state;

public class EntryPoint {
    public static void main(String[] args) {
        Order order = new Order();
        System.out.println(order.getStatus());
        order.pay();
        order.ship();
        System.out.println(order.getStatus());
        try {
            order.pay();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
