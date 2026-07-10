# プロジェクト構造

```text
ES260523874-cloud-ops-portfolio/
  backend-java/          Java REST API
  worker-python/         Python automation worker
  web-dashboard/         Vue.js dashboard
  scripts/               setup / run / test / screenshot
  docs/                  設計書、Jira/Confluence 風文書
  infra/                 AWS mock config と Terraform 例
  infra/mysql/           Docker MySQL 初期化 SQL
  screenshots/           生成 PNG
  data/                  中立的な mock データ
```

## Java backend

`controller`、`service`、`repository`、`model` に分け、基本設計と詳細設計の責務分離を見せる。

## Python worker

`api_client.py` で API 連携を分離し、`batch_worker.py` で自動化処理を実装する。

## scripts

Shell script は日語コメント付きで、セットアップ、ローカル実行、Docker 実行、テスト、スクリーンショット生成を担当する。

## infra

Terraform、mock config、MySQL init SQL により、AWS 構築思想とローカル永続化 DB を credential なしで説明する。

## screenshots

Playwright により Vite dev server `http://127.0.0.1:5173` を起動し、`dashboard.png`、`request-list.png`、`request-detail.png`、`batch-result.png`、`logs-or-tests.png` を生成する。
