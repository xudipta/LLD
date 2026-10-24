package patterns.builder;

public class EntryPoint {
    public static void main(String[] args) {
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .url("https://example.com/orders")
                .header("Content-Type", "application/json")
                .body("{\"item\":42}")
                .build();
        System.out.println(request.getMethod() + " " + request.getUrl() + " " + request.getHeaders());
    }
}
