package com.es260523874.cloudops.model;

import java.time.Instant;

/**
 * クラウドリソース申請を表すドメインモデルです。
 * 実案件名や顧客名を含めず、ポートフォリオ用の中立的な項目だけを保持します。
 */
public class CloudResourceRequest {
    public String requestId;
    public String projectName;
    public String resourceType;
    public String environment;
    public String status;
    public String requestedBy;
    public String createdAt;

    public CloudResourceRequest(String requestId, String projectName, String resourceType, String environment, String status, String requestedBy) {
        this.requestId = requestId;
        this.projectName = projectName;
        this.resourceType = resourceType;
        this.environment = environment;
        this.status = status;
        this.requestedBy = requestedBy;
        this.createdAt = Instant.now().toString();
    }
}

