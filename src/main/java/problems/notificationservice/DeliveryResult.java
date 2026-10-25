package problems.notificationservice;

public record DeliveryResult(boolean delivered, int attempts, String error) {
}
