CREATE TABLE IF NOT EXISTS cloud_resource_requests (
  request_id VARCHAR(64) PRIMARY KEY,
  project_name VARCHAR(160) NOT NULL,
  resource_type VARCHAR(64) NOT NULL,
  environment_name VARCHAR(64) NOT NULL,
  status VARCHAR(32) NOT NULL,
  requested_by VARCHAR(120) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO cloud_resource_requests (
  request_id,
  project_name,
  resource_type,
  environment_name,
  status,
  requested_by
) VALUES
  ('REQ-001', 'neutral-research-platform', 'ECS', 'dev', 'PENDING', 'platform-user'),
  ('REQ-002', 'internal-automation-hub', 'S3', 'stg', 'APPROVED', 'ops-user'),
  ('REQ-003', 'api-observability-lab', 'RDS', 'prod-like', 'RUNNING', 'devops-user')
ON DUPLICATE KEY UPDATE
  project_name = VALUES(project_name),
  resource_type = VALUES(resource_type),
  environment_name = VALUES(environment_name),
  status = VALUES(status),
  requested_by = VALUES(requested_by);
