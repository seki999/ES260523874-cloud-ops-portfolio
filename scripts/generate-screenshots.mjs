import { chromium } from "@playwright/test";
import { spawn } from "node:child_process";
import path from "node:path";
import { fileURLToPath } from "node:url";

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "..");
const dashboardUrl = "http://127.0.0.1:5173";
const outputDir = path.join(root, "screenshots");

const captures = [
  ["dashboard.png", "#dashboard"],
  ["request-list.png", "#requests"],
  ["request-detail.png", ".detail"],
  ["batch-result.png", "#batch"],
  ["logs-or-tests.png", "#logs"]
];

const devServer = spawn("npm", ["run", "dev"], {
  cwd: root,
  shell: true,
  stdio: "inherit"
});

async function waitForServer(url, attempts = 40) {
  for (let attempt = 0; attempt < attempts; attempt += 1) {
    try {
      const response = await fetch(url);
      if (response.ok) {
        return;
      }
    } catch {
      await new Promise((resolve) => setTimeout(resolve, 500));
    }
  }
  throw new Error(`${url} が起動しませんでした`);
}

try {
  await waitForServer(dashboardUrl);
  const browser = await chromium.launch();
  const page = await browser.newPage({ viewport: { width: 1440, height: 980 }, deviceScaleFactor: 1 });
  await page.goto(dashboardUrl);
  for (const [fileName, selector] of captures) {
    const locator = page.locator(selector);
    await locator.screenshot({ path: path.join(outputDir, fileName) });
  }
  await browser.close();
  console.log(`Generated ${captures.length} screenshots in ${outputDir}`);
} finally {
  devServer.kill();
}
