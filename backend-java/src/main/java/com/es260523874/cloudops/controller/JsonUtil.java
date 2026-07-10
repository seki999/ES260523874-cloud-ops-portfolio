package com.es260523874.cloudops.controller;

import com.es260523874.cloudops.model.CloudResourceRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** JSON ライブラリなしで動かすための最小限の変換処理です。 */
public class JsonUtil {
    public static String quote(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static String requestToJson(CloudResourceRequest request) {
        return "{"
            + "\"requestId\":\"" + quote(request.requestId) + "\","
            + "\"projectName\":\"" + quote(request.projectName) + "\","
            + "\"resourceType\":\"" + quote(request.resourceType) + "\","
            + "\"environment\":\"" + quote(request.environment) + "\","
            + "\"status\":\"" + quote(request.status) + "\","
            + "\"requestedBy\":\"" + quote(request.requestedBy) + "\","
            + "\"createdAt\":\"" + quote(request.createdAt) + "\""
            + "}";
    }

    public static String requestListToJson(List<CloudResourceRequest> requests) {
        return "[" + requests.stream().map(JsonUtil::requestToJson).collect(Collectors.joining(",")) + "]";
    }

    public static String mapToJson(Map<String, String> values) {
        return "{" + values.entrySet().stream()
            .map(e -> "\"" + quote(e.getKey()) + "\":\"" + quote(e.getValue()) + "\"")
            .collect(Collectors.joining(",")) + "}";
    }

    public static String readJsonValue(String body, String key) {
        String marker = "\"" + key + "\"";
        int keyIndex = body.indexOf(marker);
        if (keyIndex < 0) {
            return "";
        }
        int colon = body.indexOf(":", keyIndex);
        int firstQuote = body.indexOf("\"", colon + 1);
        int secondQuote = body.indexOf("\"", firstQuote + 1);
        if (colon < 0 || firstQuote < 0 || secondQuote < 0) {
            return "";
        }
        return body.substring(firstQuote + 1, secondQuote);
    }
}

