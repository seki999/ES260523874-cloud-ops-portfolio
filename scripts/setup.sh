#!/usr/bin/env bash
set -euo pipefail

# ローカル検証に必要な Node / Python 依存関係を準備します。
python -m pip install -r worker-python/requirements.txt
npm install

