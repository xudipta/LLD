package problems.notificationservice;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Message templates per notification type with {@code {placeholder}} substitution. */
public class TemplateRegistry {
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{(\\w+)}");
    private final Map<String, String> templates = new HashMap<>();

    public void register(String type, String template) {
        templates.put(type, template);
    }

    public String render(String type, Map<String, String> params) {
        String template = templates.get(type);
        if (template == null) {
            throw new IllegalArgumentException("No template for type: " + type);
        }
        Matcher m = PLACEHOLDER.matcher(template);
        StringBuilder out = new StringBuilder();
        while (m.find()) {
            String value = params.get(m.group(1));
            if (value == null) {
                throw new IllegalArgumentException("Missing parameter: " + m.group(1));
            }
            m.appendReplacement(out, Matcher.quoteReplacement(value));
        }
        m.appendTail(out);
        return out.toString();
    }
}
