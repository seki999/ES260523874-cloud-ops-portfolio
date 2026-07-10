# セットアップ手順

## 前提

- Python 3.13
- Node.js 20
- Docker
- Docker Compose 上の MySQL 8.4
- Java 21 JDK: Java API をローカルで直接動かす場合のみ必要

## 環境構築

```bash
cp .env.example .env
bash scripts/setup.sh
```

## Frontend 起動

Vue.js dashboard は固定 port `5173` で起動する。

```bash
bash scripts/run-frontend.sh
```

ブラウザで `http://127.0.0.1:5173` を開く。

## Java API ローカル実行

Java/JDK がある環境:

```bash
bash scripts/run-local.sh
```

## Docker 実行

起動:

```bash
bash scripts/run-docker.sh
```

起動後、Vue.js dashboard は `http://127.0.0.1:5173` で利用できます。MySQL は `localhost:3306`、database は `cloudops`、user は `cloudops_user` で利用できます。初期 table と seed data は `infra/mysql/init.sql` にあります。

停止:

```bash
docker compose down
```

MySQL volume も削除して初期状態に戻す場合:

```bash
docker compose down -v
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
