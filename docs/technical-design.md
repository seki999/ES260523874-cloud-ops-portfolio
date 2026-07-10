# 詳細設計

## 技術説明

案件に明示された Java、Python、AWS、Docker、Shell、Git、RESTful API、DevOps 設計、テスト、設計書作成を中心に構成する。Spring Boot、DB、GitHub Actions、Terraform、Web Dashboard はポートフォリオとして完成度を上げるための追加候補として README に明記している。本実装の Java API は JDK 標準 HttpServer を使い、ローカル環境の Maven 不在でも設計を読める形にした。

## 主要モジュール

- `CloudOpsApplication`: Java API 起動処理。
- `RequestController`: HTTP method と path を判定する controller。
- `RequestService`: 申請作成、一覧、詳細、ステータス更新の業務ロジック。
- `RequestRepository`: in-memory データ管理。
- `batch_worker.py`: PENDING 申請の処理とログ出力。
- `api_client.py`: Java API 連携。

## DB 設計

ローカルでは in-memory repository を使う。永続化する場合は RDS PostgreSQL または DynamoDB に以下の項目を保存する。

| column | type | note |
|---|---|---|
| requestId | string | 申請 ID |
| projectName | string | 中立的なプロジェクト名 |
| resourceType | string | ECS / S3 / RDS など |
| environment | string | dev / stg / prod-like |
| status | string | PENDING / APPROVED / RUNNING / COMPLETED |
| requestedBy | string | 実名ではないロール名 |
| createdAt | timestamp | 作成日時 |

## API 設計

エラー時は `{"error":"not_found"}` のように JSON で返す。実運用では validation error、trace id、監査ログ連携を追加する。

## ログ設計

Python worker は JSONL で CloudWatch 風のログを出力する。credential、個人情報、顧客名は出力しない。

## テスト設計

- Python: worker の処理対象判定とログ生成を pytest で確認。
- Java: service 層の作成、取得、更新を最小テストで確認。
- CI: Python、Java、スクリーンショット生成を同一 workflow で検証。

## Java API と Python worker の連動

worker は `WORKER_API_URL` を読み、`GET /api/requests` で PENDING 申請を取得し、`PUT /api/requests/{id}/status` で処理結果を戻す。

## AWS mock / config

`infra/aws-mock-config.yaml` はローカル Docker と AWS サービスの対応関係を示す。実 AWS credential は不要で、Terraform は設計説明用の最小構成として置く。

