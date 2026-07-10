"""AWS 構築処理を mock する Python batch worker です。"""

from __future__ import annotations

import json
import os
from datetime import UTC, datetime
from pathlib import Path

from .api_client import ApiClient


def build_execution_log(request: dict[str, str], status: str) -> dict[str, str]:
    """CloudWatch 風の実行ログを作ります。機密情報を含めない形に限定しています。"""
    return {
        "timestamp": datetime.now(UTC).isoformat(),
        "requestId": request["requestId"],
        "resourceType": request["resourceType"],
        "environment": request["environment"],
        "resultStatus": status,
        "message": "Mock AWS provisioning completed without real cloud access.",
    }


def process_pending_requests(client: ApiClient) -> list[dict[str, str]]:
    """PENDING の申請だけを対象に、AWS 構築が完了した想定でステータスを更新します。"""
    logs: list[dict[str, str]] = []
    for request in client.list_requests():
        if request.get("status") != "PENDING":
            continue
        client.update_status(request["requestId"], "COMPLETED")
        logs.append(build_execution_log(request, "COMPLETED"))
    return logs


def write_logs(logs: list[dict[str, str]], output_path: Path) -> None:
    """README とスクリーンショットで参照しやすい JSONL 形式でログを保存します。"""
    output_path.parent.mkdir(parents=True, exist_ok=True)
    output_path.write_text("\n".join(json.dumps(row, ensure_ascii=False) for row in logs), encoding="utf-8")


def main() -> None:
    """環境変数から接続先を読み、1 回だけ batch を実行します。"""
    base_url = os.getenv("WORKER_API_URL", "http://localhost:8080")
    output_path = Path(os.getenv("WORKER_LOG_PATH", "logs/worker-execution.jsonl"))
    logs = process_pending_requests(ApiClient(base_url))
    write_logs(logs, output_path)
    print(json.dumps({"processed": len(logs), "logPath": str(output_path)}, ensure_ascii=False))


if __name__ == "__main__":
    main()

