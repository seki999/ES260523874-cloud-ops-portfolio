from worker.batch_worker import build_execution_log, process_pending_requests


class FakeClient:
    def __init__(self):
        self.updated = []

    def list_requests(self):
        return [
            {"requestId": "REQ-001", "resourceType": "ECS", "environment": "dev", "status": "PENDING"},
            {"requestId": "REQ-002", "resourceType": "S3", "environment": "stg", "status": "APPROVED"},
        ]

    def update_status(self, request_id, status):
        self.updated.append((request_id, status))
        return {"requestId": request_id, "status": status}


def test_build_execution_log_masks_real_cloud_access():
    log = build_execution_log({"requestId": "REQ-001", "resourceType": "ECS", "environment": "dev"}, "COMPLETED")
    assert log["requestId"] == "REQ-001"
    assert "Mock AWS" in log["message"]


def test_process_pending_requests_updates_only_pending():
    client = FakeClient()
    logs = process_pending_requests(client)
    assert client.updated == [("REQ-001", "COMPLETED")]
    assert len(logs) == 1

