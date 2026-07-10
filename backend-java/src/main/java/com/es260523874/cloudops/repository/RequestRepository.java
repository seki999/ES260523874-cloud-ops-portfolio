package com.es260523874.cloudops.repository;

import com.es260523874.cloudops.model.CloudResourceRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ローカル検証を簡単にするためのインメモリ repository です。
 * 永続 DB は README で拡張候補として扱い、ここでは API 動作確認を優先します。
 */
public class RequestRepository implements CloudResourceRequestRepository {
    private final ConcurrentHashMap<String, CloudResourceRequest> store = new ConcurrentHashMap<>();

    public RequestRepository() {
        save(new CloudResourceRequest("REQ-001", "neutral-research-platform", "ECS", "dev", "PENDING", "platform-user"));
        save(new CloudResourceRequest("REQ-002", "internal-automation-hub", "S3", "stg", "APPROVED", "ops-user"));
        save(new CloudResourceRequest("REQ-003", "api-observability-lab", "RDS", "prod-like", "RUNNING", "devops-user"));
    }

    @Override
    public List<CloudResourceRequest> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<CloudResourceRequest> findById(String requestId) {
        return Optional.ofNullable(store.get(requestId));
    }

    @Override
    public CloudResourceRequest save(CloudResourceRequest request) {
        store.put(request.requestId, request);
        return request;
    }

    @Override
    public Optional<CloudResourceRequest> updateStatus(String requestId, String status) {
        CloudResourceRequest request = store.get(requestId);
        if (request == null) {
            return Optional.empty();
        }
        request.status = status;
        return Optional.of(request);
    }
}
