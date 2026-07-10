# セットアップ手順

## 前提

- Python 3.13
- Node.js 20
- Docker
- Java 21 JDK: Java API をローカルで直接動かす場合のみ必要

## 環境構築

```bash
cp .env.example .env
bash scripts/setup.sh
```

## ローカル実行

Java/JDK がある環境:

```bash
bash scripts/run-local.sh
```

Dashboard は `web-dashboard/index.html` をブラウザで開く。

## Docker 実行

```bash
bash scripts/run-docker.sh
```

## テスト

```bash
bash scripts/test.sh
```

JDK がない場合、Java test は skip される。GitHub Actions では Temurin 21 を入れて Java compile/test を実行する。

## AWS mock / config

`infra/aws-mock-config.yaml` はローカル component と AWS サービスの対応を示す。実 AWS へ接続しないため、credential は不要。

## よくある問題

- `java` / `javac` がない: Docker または GitHub Actions で検証する。
- Playwright browser がない: `npm run screenshots` が Chromium を導入する。
- Docker 起動に失敗する: `docker compose logs` で `java-api` healthcheck を確認する。

