# Jira 風 Ticket 一覧

| Key | 種別 | 概要 | 受入条件 |
|---|---|---|---|
| ES260523874-REQ-001 | 需求具体化 | クラウド申請、batch、自動化ログの範囲を定義する | README に対象機能と非対象機能が明記される |
| ES260523874-BD-001 | 基本設計 | AWS / Java API / Python worker / Docker の構成を整理する | architecture.md に構成図とデータ流がある |
| ES260523874-DD-001 | 詳細設計 | API、DB、ログ、エラー処理を設計する | technical-design.md に API と項目一覧がある |
| ES260523874-JAVA-001 | 実装 | Java REST API を実装する | health、list、detail、create、status update がある |
| ES260523874-PY-001 | 実装 | Python worker を実装する | PENDING 申請のみ処理し、ログを出す |
| ES260523874-DOC-001 | 環境構築 | Docker Compose と Shell script を整える | `scripts/run-docker.sh` で起動できる |
| ES260523874-TEST-001 | テスト | Java/Python の基本テストを用意する | `scripts/test.sh` と CI で確認できる |
| ES260523874-README-001 | 文書整備 | README とスクリーンショットを整備する | README に 5 枚の画像が埋め込まれる |

## テスト記録

- Python worker: PENDING のみ処理することを pytest で確認。
- Java service: 作成、取得、更新の最小テストを用意。
- Screenshot: Playwright で UI の 5 領域を PNG 化。

