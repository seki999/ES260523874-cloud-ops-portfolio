# Confluence 風 設計ノート

## 背景

クラウドリソース申請、自動化 batch、API 管理、運用監視を一つの中立的な題材としてまとめ、AWS / Java / Python / DevOps の経験を説明できるようにする。

## 目的

実顧客名や機密情報を使わず、設計から実装、テスト、ドキュメント作成までの流れを GitHub 上で確認できる状態にする。

## システム概要

Java API が申請管理を担当し、Python worker が pending 申請を処理する。Dashboard は状態確認と README 画像用に提供する。

## アーキテクチャ方針

ローカルでは Docker Compose、設計上は AWS API Gateway / ECS / S3 / RDS / CloudWatch / IAM へ展開できる構成にする。

## API 方針

RESTful API とし、resource を `/api/requests` に集約する。status 更新は batch 連携を意識して専用 endpoint にする。

## AWS 設計方針

実 AWS へ接続しない。mock config と Terraform 例で構成意図だけを表現し、credential は管理対象外にする。

## DevOps 方針

GitHub Actions で build/test/screenshot smoke check を実施する。branch は `feature/*` から Pull Request を作り、review 後に `develop`、安定版を `main` へ反映する。

## テスト方針

小さな unit test と endpoint health check から始める。拡張時は API contract test、Docker smoke test、E2E test を追加する。

## 今後課題

認証、RDS 永続化、LocalStack 連携、監査ログ、Terraform module 化、OpenAPI 定義、Dashboard の API 接続を追加する。

