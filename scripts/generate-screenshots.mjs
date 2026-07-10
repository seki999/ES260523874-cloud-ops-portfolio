import { chromium } from "@playwright/test";
import { spawn } from "node:child_process";
import path from "node:path";
import { fileURLToPath } from "node:url";

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "..");
const dashboardUrl = "http://127.0.0.1:5173";
const outputDir = path.join(root, "screenshots");

const captures = [
  ["dashboard.png", "dashboard", "#dashboard"],
  ["request-list.png", "requests", "#requests"],
  ["request-detail.png", "batch", ".detail"],
  ["batch-result.png", "batch", "#batch"],
  ["logs-or-tests.png", "logs", "#logs"]
];

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
  throw new Error(`${url} did not start`);
}

async function isServerAvailable(url) {
  try {
    const response = await fetch(url);
    return response.ok;
  } catch {
    return false;
  }
}

const devServer = (await isServerAvailable(dashboardUrl))
  ? null
  : spawn("npm", ["run", "dev"], {
      cwd: root,
      shell: true,
      stdio: "inherit"
    });

let browser;
try {
  await waitForServer(dashboardUrl);
  browser = await chromium.launch();
  const page = await browser.newPage({ viewport: { width: 1440, height: 980 }, deviceScaleFactor: 1 });
  for (const [fileName, viewHash, selector] of captures) {
    await page.goto(`${dashboardUrl}/#${viewHash}`);
    const locator = page.locator(selector);
    await locator.screenshot({ path: path.join(outputDir, fileName) });
  }
  console.log(`Generated ${captures.length} screenshots in ${outputDir}`);
} finally {
  if (browser) {
    await browser.close();
  }
  if (devServer) {
    devServer.kill();
  }
}
