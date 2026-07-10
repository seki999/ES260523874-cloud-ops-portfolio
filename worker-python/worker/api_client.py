"""Java API と連携するための薄い client です。"""

from __future__ import annotations

from dataclasses import dataclass
import time
from typing import Any

import requests


@dataclass
class ApiClient:
    """申請一覧取得とステータス更新だけに責務を絞った API client。"""

    base_url: str
    retries: int = 30
    retry_interval_seconds: float = 1.0

    def list_requests(self) -> list[dict[str, Any]]:
        """Java API からクラウドリソース申請の一覧を取得します。"""
        last_error: Exception | None = None
        for _ in range(self.retries):
            try:
                response = requests.get(f"{self.base_url}/api/requests", timeout=5)
                response.raise_for_status()
                return response.json()
            except requests.RequestException as exc:
                # Docker Compose では API コンテナ起動直後に worker が先行することがあるため、短く再試行します。
                last_error = exc
                time.sleep(self.retry_interval_seconds)
        raise RuntimeError("Java API に接続できませんでした") from last_error

    def update_status(self, request_id: str, status: str) -> dict[str, Any]:
        """自動化処理結果を Java API へ反映します。"""
        response = requests.put(
            f"{self.base_url}/api/requests/{request_id}/status",
            json={"status": status},
            timeout=5,
        )
        response.raise_for_status()
        return response.json()
