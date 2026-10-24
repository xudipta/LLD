package patterns.builder;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class HttpRequest {
    private final String method;
    private final String url;
    private final Map<String, String> headers;
    private final String body;

    private HttpRequest(Builder b) {
        this.method = b.method;
        this.url = b.url;
        this.headers = Collections.unmodifiableMap(new LinkedHashMap<>(b.headers));
        this.body = b.body;
    }

    public String getMethod() { return method; }
    public String getUrl() { return url; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String method = "GET";
        private String url;
        private final Map<String, String> headers = new LinkedHashMap<>();
        private String body;

        public Builder method(String method) { this.method = method; return this; }
        public Builder url(String url) { this.url = url; return this; }
        public Builder header(String name, String value) { headers.put(name, value); return this; }
        public Builder body(String body) { this.body = body; return this; }

        /** Validation happens once, here, so an HttpRequest is always valid. */
        public HttpRequest build() {
            if (url == null || url.isBlank()) {
                throw new IllegalStateException("url is required");
            }
            if (body != null && method.equals("GET")) {
                throw new IllegalStateException("GET requests cannot have a body");
            }
            return new HttpRequest(this);
        }
    }
}
