# ES260523874-cloud-ops-portfolio

研究開発向けクラウド運用管理ポータルを題材にした GitHub portfolio project です。AWS クラウド設計、Java REST API、Python 自動化 worker、Docker、Shell、Git / DevOps、Jira / Confluence 風ドキュメント、基本設計からテストまでの流れを一つのリポジトリで確認できるようにしています。

## 対応 job 技術スタック

 Java、Python、AWS、Git、Shell、Docker、RESTful API、Jira、Confluence、DevOps 環境設計、後端開発、基本設計、詳細設計、実装、テスト、設計書作成、仕様設計図作成、需求具体化。

ポートフォリオ完成度のために追加した技術: JDK 標準 HttpServer、Playwright、GitHub Actions、Terraform、静的 Web Dashboard。これらは原案件で明示指定されたものではなく、動作確認・CI・設計説明・スクリーンショット作成を補うために追加しています。

## システム架構図

```mermaid
flowchart LR
  User["利用者"] --> Web["Web Dashboard"]
  Web --> Java["Java REST API"]
  Worker["Python Worker"] --> Java
  Java --> Store["In-memory Store"]
  Worker --> Log["JSONL Logs"]
  Java -.設計上.-> AWS["AWS API Gateway / ECS / S3 / RDS / CloudWatch / IAM"]
```

## ディレクトリ構造

```text
backend-java/      Java REST API
worker-python/     Python automation worker
web-dashboard/     UI and screenshot source
scripts/           setup / run / test / screenshot scripts
docs/              architecture and project documents
infra/             AWS mock config and Terraform notes
screenshots/       README images
data/              neutral mock request data
```

## ローカル実行

```bash
cp .env.example .env
bash scripts/setup.sh
bash scripts/run-local.sh
```

`GET http://localhost:8080/api/health` で API を確認します。Java/JDK がない環境では Docker または GitHub Actions で Java 側を検証してください。

## Docker 実行

```bash
bash scripts/run-docker.sh
```

Java API と Python worker が Docker Compose で起動します。

## テスト

```bash
bash scripts/test.sh
```

Python worker は pytest、Java service は JDK がある場合に `javac` と `java` で確認します。GitHub Actions では Temurin 21 を使って Java 側も実行します。

## 主要機能

- Dashboard: 申請件数、API 状態、worker 状態、ログを表示。
- Cloud resource request API: 申請一覧、詳細、作成、ステータス更新。
- Python automation: PENDING 申請を取得し、AWS 構築処理を mock 実行。
- AWS design: API Gateway、ECS、S3、RDS、CloudWatch、IAM の設計方針を文書化。
- DevOps: Docker Compose、Shell script、GitHub Actions、branch strategy。

## 技術選型理由

Java API は backend 開発と RESTful API 設計を示すために採用しました。Python worker は自動化・batch 処理を示すために分離しています。Docker Compose は複数 component の起動確認、Terraform は AWS 設計方針の説明、Playwright は README に貼る実行画面を再生成するために使います。

## セキュリティ考慮

- AWS credential、secret、個人情報、実組織名は配置しない。
- `.env.example` のみ管理し、`.env` は `.gitignore` に入れる。
- IAM は最小権限を前提にし、API / worker / log access の role を分離する。
- worker log は requestId、resourceType、environment など運用に必要な項目だけに限定する。
- 本番化する場合は Cognito / OIDC、JWT、監査ログ、入力 validation、rate limit を追加する。

## 運用スクリーンショット

`npm run screenshots` または `bash scripts/generate-screenshots.sh` で以下の PNG を生成します。

![Dashboard](screenshots/dashboard.png)
![Request list](screenshots/request-list.png)
![Request detail](screenshots/request-detail.png)
![Batch result](screenshots/batch-result.png)
![Logs and tests](screenshots/logs-or-tests.png)

## Git / DevOps 方針

- `main`: 安定版。
- `develop`: 結合確認用。
- `feature/*`: ticket 単位の作業 branch。
- Pull Request で CI を通してから merge する。

GitHub Actions はポートフォリオとして DevOps 設計を見せるために追加した内容です。

## 今後の拡張

- OpenAPI 定義と API contract test。
- RDS PostgreSQL または DynamoDB Local 連携。
- LocalStack による S3 / SQS / CloudWatch mock。
- JWT 認証と role-based access control。
- Dashboard から実 API を呼ぶ frontend 化。
- Terraform module 化と環境別 tfvars 設計。

## 関連ドキュメント

- [アーキテクチャ設計](docs/architecture.md)
- [詳細設計](docs/technical-design.md)
- [プロジェクト構造](docs/project-structure.md)
- [セットアップ](docs/setup.md)
- [Jira 風 ticket](docs/jira-sample-tickets.md)
- [Confluence 風設計ノート](docs/confluence-design-note.md)

