package com.es260523874.cloudops;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/** Docker Compose の healthcheck から Java API の readiness を確認します。 */
public class HealthCheck {
    public static void main(String[] args) throws Exception {
        String port = System.getenv().getOrDefault("API_PORT", "8080");
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + "/api/health"))
            .timeout(Duration.ofSeconds(3))
            .GET()
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            System.exit(1);
        }
    }
}
