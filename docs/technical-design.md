# 詳細設計

## 技術説明

案件に明示された Java、Python、AWS、Docker、Shell、Git、RESTful API、DevOps 設計、テスト、設計書作成を中心に構成する。Vue.js、Vite、MySQL、GitHub Actions、Terraform、Web Dashboard はポートフォリオとして完成度を上げるための追加候補として README に明記している。本実装の Java API は JDK 標準 HttpServer を使い、ローカル環境の Maven 不在でも設計を読める形にした。

## 主要モジュール

- `CloudOpsApplication`: Java API 起動処理。
- `RequestController`: HTTP method と path を判定する controller。
- `RequestService`: 申請作成、一覧、詳細、ステータス更新の業務ロジック。
- `RequestRepository`: in-memory データ管理。
- `MySqlRequestRepository`: Docker MySQL に対する JDBC repository。
- `batch_worker.py`: PENDING 申請の処理とログ出力。
- `api_client.py`: Java API 連携。
- `web-dashboard/src/App.vue`: Vite dev server の `5173` port で起動する Vue.js dashboard。

## DB 設計

Docker Compose では MySQL を使う。schema と seed data は `infra/mysql/init.sql` で初期化する。Java/JDK だけで API を確認したい場合は `DB_HOST` 未設定にして in-memory repository に fallback できる。

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
Docker 環境では Java API が MySQL に更新結果を保存するため、worker 実行後の状態を API から再取得できる。

## AWS mock / config

`infra/aws-mock-config.yaml` はローカル Docker と AWS サービスの対応関係を示す。実 AWS credential は不要で、Terraform は設計説明用の最小構成として置く。
