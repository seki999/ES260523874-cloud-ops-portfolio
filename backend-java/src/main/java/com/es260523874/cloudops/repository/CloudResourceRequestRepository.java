package com.es260523874.cloudops.repository;

import com.es260523874.cloudops.model.CloudResourceRequest;
import java.util.List;
import java.util.Optional;

/** 申請データの保存先を MySQL / in-memory で差し替えるための interface です。 */
public interface CloudResourceRequestRepository {
    List<CloudResourceRequest> findAll();

    Optional<CloudResourceRequest> findById(String requestId);

    CloudResourceRequest save(CloudResourceRequest request);

    Optional<CloudResourceRequest> updateStatus(String requestId, String status);
}
