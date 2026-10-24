package patterns.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {
    @Test
    void buildsWithDefaults() {
        HttpRequest r = HttpRequest.builder().url("http://x").build();
        assertEquals("GET", r.getMethod());
        assertTrue(r.getHeaders().isEmpty());
    }

    @Test
    void buildsFullRequest() {
        HttpRequest r = HttpRequest.builder().method("POST").url("http://x")
                .header("A", "1").body("b").build();
        assertEquals("POST", r.getMethod());
        assertEquals("1", r.getHeaders().get("A"));
        assertEquals("b", r.getBody());
    }

    @Test
    void urlIsRequired() {
        assertThrows(IllegalStateException.class, () -> HttpRequest.builder().build());
    }

    @Test
    void getCannotHaveBody() {
        assertThrows(IllegalStateException.class,
                () -> HttpRequest.builder().url("http://x").body("b").build());
    }

    @Test
    void headersAreImmutable() {
        HttpRequest r = HttpRequest.builder().url("http://x").build();
        assertThrows(UnsupportedOperationException.class, () -> r.getHeaders().put("A", "1"));
    }
}
