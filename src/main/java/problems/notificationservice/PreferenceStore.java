package problems.notificationservice;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Which channels a user wants for each notification type. With no preference set, every channel is allowed. */
public class PreferenceStore {
    private final Map<String, Set<Channel>> preferences = new HashMap<>();

    public void set(String userId, String type, Collection<Channel> channels) {
        EnumSet<Channel> chosen = EnumSet.noneOf(Channel.class);
        chosen.addAll(channels);
        preferences.put(key(userId, type), chosen);
    }

    public void optOut(String userId, String type) {
        set(userId, type, Set.of());
    }

    public Set<Channel> channelsFor(String userId, String type) {
        Set<Channel> chosen = preferences.get(key(userId, type));
        EnumSet<Channel> result = EnumSet.noneOf(Channel.class);
        result.addAll(chosen == null ? EnumSet.allOf(Channel.class) : chosen);
        return result;
    }

    private static String key(String userId, String type) {
        return userId + "|" + type;
    }
}
