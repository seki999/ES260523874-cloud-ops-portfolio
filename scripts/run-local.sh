#!/usr/bin/env bash
set -euo pipefail

# Java が導入済みの環境では、JDK 標準 HttpServer ベースの API を起動できます。
find backend-java/src/main/java -name "*.java" > /tmp/es260523874-java-sources.txt
javac -encoding UTF-8 -d backend-java/out @/tmp/es260523874-java-sources.txt
java -cp backend-java/out com.es260523874.cloudops.CloudOpsApplication

