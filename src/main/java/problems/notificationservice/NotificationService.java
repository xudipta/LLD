package problems.notificationservice;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Renders a template, then delivers it on every channel the user has enabled. Each channel
 * is retried independently and a failing channel never stops the others.
 */
public class NotificationService {
    private final Map<Channel, NotificationChannel> channels = new EnumMap<>(Channel.class);
    private final TemplateRegistry templates;
    private final PreferenceStore preferences;
    private final int maxAttempts;

    public NotificationService(List<NotificationChannel> channels, TemplateRegistry templates,
                               PreferenceStore preferences, int maxAttempts) {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be at least 1");
        }
        channels.forEach(c -> this.channels.put(c.channel(), c));
        this.templates = templates;
        this.preferences = preferences;
        this.maxAttempts = maxAttempts;
    }

    /** @return one result per channel that was attempted (channels the user opted out of are absent). */
    public Map<Channel, DeliveryResult> send(String userId, String type, Map<String, String> params) {
        String message = templates.render(type, params);     // fail fast on a bad template or missing parameter
        Map<Channel, DeliveryResult> report = new EnumMap<>(Channel.class);
        for (Channel channel : preferences.channelsFor(userId, type)) {
            NotificationChannel sender = channels.get(channel);
            if (sender != null) {
                report.put(channel, deliver(sender, userId, message));
            }
        }
        return report;
    }

    private DeliveryResult deliver(NotificationChannel sender, String userId, String message) {
        String lastError = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                sender.send(userId, message);
                return new DeliveryResult(true, attempt, null);
            } catch (DeliveryException e) {
                lastError = e.getMessage();
            }
        }
        return new DeliveryResult(false, maxAttempts, lastError);
    }
}
