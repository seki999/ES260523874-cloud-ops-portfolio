#!/usr/bin/env bash
set -euo pipefail

# Java API と Python worker を Docker Compose でまとめて起動します。
docker compose up --build

