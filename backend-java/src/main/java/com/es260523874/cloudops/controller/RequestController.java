package com.es260523874.cloudops.controller;

import com.es260523874.cloudops.model.CloudResourceRequest;
import com.es260523874.cloudops.service.RequestService;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * RESTful API のルーティングを担当します。
 * Spring Boot がない環境でも読めるよう、JDK 標準の HttpServer で API 仕様を表現しています。
 */
public class RequestController {
    private final RequestService service;

    public RequestController(RequestService service) {
        this.service = service;
    }

    public void handleHealth(HttpExchange exchange) throws IOException {
        sendJson(exchange, 200, JsonUtil.mapToJson(Map.of("status", "UP", "service", "ES260523874 Java API")));
    }

    public void handleRequests(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if ("GET".equals(method) && "/api/requests".equals(path)) {
            sendJson(exchange, 200, JsonUtil.requestListToJson(service.listRequests()));
            return;
        }
        if ("POST".equals(method) && "/api/requests".equals(path)) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            CloudResourceRequest created = service.createRequest(
                JsonUtil.readJsonValue(body, "projectName"),
                JsonUtil.readJsonValue(body, "resourceType"),
                JsonUtil.readJsonValue(body, "environment"),
                JsonUtil.readJsonValue(body, "requestedBy")
            );
            sendJson(exchange, 201, JsonUtil.requestToJson(created));
            return;
        }

        String requestId = path.replace("/api/requests/", "").replace("/status", "");
        if ("GET".equals(method) && path.matches("/api/requests/[^/]+")) {
            Optional<CloudResourceRequest> request = service.getRequest(requestId);
            sendJson(exchange, request.isPresent() ? 200 : 404, request.map(JsonUtil::requestToJson).orElse("{\"error\":\"not_found\"}"));
            return;
        }
        if ("PUT".equals(method) && path.matches("/api/requests/[^/]+/status")) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Optional<CloudResourceRequest> updated = service.updateStatus(requestId, JsonUtil.readJsonValue(body, "status"));
            sendJson(exchange, updated.isPresent() ? 200 : 404, updated.map(JsonUtil::requestToJson).orElse("{\"error\":\"not_found\"}"));
            return;
        }
        sendJson(exchange, 404, "{\"error\":\"not_found\"}");
    }

    private void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}

