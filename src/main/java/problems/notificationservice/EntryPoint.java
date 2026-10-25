package problems.notificationservice;

import java.util.List;
import java.util.Map;

public class EntryPoint {
    public static void main(String[] args) {
        TemplateRegistry templates = new TemplateRegistry();
        templates.register("ORDER_SHIPPED", "Hi {name}, order {orderId} has shipped.");
        NotificationService service = new NotificationService(
                List.of(new ConsoleChannel(Channel.EMAIL), new ConsoleChannel(Channel.SMS)),
                templates, new PreferenceStore(), 3);
        System.out.println(service.send("u1", "ORDER_SHIPPED", Map.of("name", "Asha", "orderId", "A-17")));
    }
}
