package problems.notificationservice;

public interface NotificationChannel {
    Channel channel();

    /** @throws DeliveryException if the message could not be delivered */
    void send(String userId, String message);
}
