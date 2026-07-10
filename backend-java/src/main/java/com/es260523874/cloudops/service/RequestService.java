package com.es260523874.cloudops.service;

import com.es260523874.cloudops.model.CloudResourceRequest;
import com.es260523874.cloudops.repository.CloudResourceRequestRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 申請 API の業務ロジックをまとめます。
 * Controller から repository を直接触らないことで、詳細設計の責務分離を見せます。
 */
public class RequestService {
    private final CloudResourceRequestRepository repository;

    public RequestService(CloudResourceRequestRepository repository) {
        this.repository = repository;
    }

    public List<CloudResourceRequest> listRequests() {
        return repository.findAll();
    }

    public Optional<CloudResourceRequest> getRequest(String requestId) {
        return repository.findById(requestId);
    }

    public CloudResourceRequest createRequest(String projectName, String resourceType, String environment, String requestedBy) {
        String requestId = "REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        CloudResourceRequest request = new CloudResourceRequest(requestId, projectName, resourceType, environment, "PENDING", requestedBy);
        return repository.save(request);
    }

    public Optional<CloudResourceRequest> updateStatus(String requestId, String status) {
        return repository.updateStatus(requestId, status);
    }
}
