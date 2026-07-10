<script setup>
import { computed, onMounted, onUnmounted, ref } from "vue";

const views = [
  { id: "dashboard", label: "Dashboard" },
  { id: "requests", label: "Requests" },
  { id: "batch", label: "Batch" },
  { id: "logs", label: "Logs" }
];

const requests = [
  {
    requestId: "REQ-001",
    projectName: "neutral-research-platform",
    resourceType: "ECS",
    environment: "dev",
    status: "PENDING",
    requestedBy: "platform-user"
  },
  {
    requestId: "REQ-002",
    projectName: "internal-automation-hub",
    resourceType: "S3",
    environment: "stg",
    status: "APPROVED",
    requestedBy: "ops-user"
  },
  {
    requestId: "REQ-003",
    projectName: "api-observability-lab",
    resourceType: "RDS",
    environment: "prod-like",
    status: "RUNNING",
    requestedBy: "devops-user"
  }
];

const metrics = [
  { label: "申請件数", value: "18", note: "今週 +4" },
  { label: "自動化成功率", value: "96%", note: "mock worker" },
  { label: "API latency", value: "42ms", note: "local" },
  { label: "未処理", value: "3", note: "PENDING" }
];

const logs = [
  "2026-07-10T09:00:00Z health check passed: /api/health",
  "2026-07-10T09:00:03Z worker processed REQ-001 as COMPLETED",
  "2026-07-10T09:00:05Z CloudWatch-style metric emitted: provisioning_success=1"
];

const currentView = ref("dashboard");

const pageTitle = computed(() => views.find((view) => view.id === currentView.value)?.label ?? "Dashboard");

const setViewFromHash = () => {
  const hash = window.location.hash.replace("#", "");
  currentView.value = views.some((view) => view.id === hash) ? hash : "dashboard";
};

const statusClass = (status) => ({
  pending: status === "PENDING",
  ok: status === "APPROVED",
  run: status === "RUNNING"
});

onMounted(() => {
  setViewFromHash();
  window.addEventListener("hashchange", setViewFromHash);
});

onUnmounted(() => {
  window.removeEventListener("hashchange", setViewFromHash);
});
</script>

<template>
  <aside>
    <strong>ES260523874</strong>
    <nav aria-label="Dashboard navigation">
      <a
        v-for="view in views"
        :key="view.id"
        :href="`#${view.id}`"
        :class="{ active: currentView === view.id }"
      >
        {{ view.label }}
      </a>
    </nav>
  </aside>

  <main>
    <header class="page-header">
      <div>
        <p class="eyebrow">研究開発向けクラウド運用管理ポータル</p>
        <h1>{{ pageTitle }}</h1>
      </div>
      <div class="status-pill">Vue Frontend: 5173 / API: 8080 / AWS: Mock</div>
    </header>

    <template v-if="currentView === 'dashboard'">
      <section id="dashboard" class="hero">
        <div>
          <p class="eyebrow">Operations Overview</p>
          <h2>Cloud resource request and DevOps operations dashboard</h2>
        </div>
        <p class="hero-copy">クラウド申請、API 稼働状況、Python worker の処理状態を一画面で確認します。</p>
      </section>

      <section class="metrics" aria-label="operation metrics">
        <article v-for="metric in metrics" :key="metric.label">
          <span>{{ metric.label }}</span>
          <strong>{{ metric.value }}</strong>
          <small>{{ metric.note }}</small>
        </article>
      </section>
    </template>

    <section v-else-if="currentView === 'requests'" id="requests" class="panel">
      <div class="section-title">
        <h2>クラウドリソース申請一覧</h2>
        <button type="button">New Request</button>
      </div>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Project</th>
            <th>Type</th>
            <th>Env</th>
            <th>Status</th>
            <th>Requested by</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="request in requests" :key="request.requestId">
            <td>{{ request.requestId }}</td>
            <td>{{ request.projectName }}</td>
            <td>{{ request.resourceType }}</td>
            <td>{{ request.environment }}</td>
            <td><b :class="statusClass(request.status)">{{ request.status }}</b></td>
            <td>{{ request.requestedBy }}</td>
          </tr>
        </tbody>
      </table>
    </section>

    <template v-else-if="currentView === 'batch'">
      <section id="batch" class="grid">
        <article class="panel detail">
          <h2>申請詳細 / API Result</h2>
          <pre>{
  "requestId": "REQ-001",
  "resourceType": "ECS",
  "environment": "dev",
  "status": "PENDING"
}</pre>
        </article>
        <article class="panel">
          <h2>Python batch execution</h2>
          <ol>
            <li>PENDING 申請を取得</li>
            <li>AWS 構築処理を mock 実行</li>
            <li>COMPLETED へステータス更新</li>
          </ol>
          <div class="progress" aria-label="batch progress"><span></span></div>
        </article>
      </section>
    </template>

    <section v-else id="logs" class="panel">
      <h2>監視 / 実行ログ</h2>
      <div class="logs">
        <p v-for="log in logs" :key="log">{{ log }}</p>
      </div>
    </section>
  </main>
</template>
