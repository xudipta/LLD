package problems.notificationservice;

public class ConsoleChannel implements NotificationChannel {
    private final Channel channel;

    public ConsoleChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public void send(String userId, String message) {
        System.out.println("[" + channel + "] to " + userId + ": " + message);
    }
}
