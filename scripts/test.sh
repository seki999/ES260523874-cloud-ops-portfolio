#!/usr/bin/env bash
set -euo pipefail

# Python worker の単体テストを実行します。
python -m pytest worker-python/tests

# Java が入っている環境では Java 側の最小テストも実行します。
if command -v javac >/dev/null 2>&1 && command -v java >/dev/null 2>&1; then
  find backend-java/src/main/java backend-java/src/test/java -name "*.java" > /tmp/es260523874-test-sources.txt
  javac -encoding UTF-8 -d backend-java/out @/tmp/es260523874-test-sources.txt
  java -cp backend-java/out com.es260523874.cloudops.RequestServiceTest
else
  echo "Java/JDK がないため Java テストをスキップしました。"
fi

