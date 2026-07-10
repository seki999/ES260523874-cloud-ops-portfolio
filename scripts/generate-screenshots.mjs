import { chromium } from "@playwright/test";
import path from "node:path";
import { fileURLToPath } from "node:url";

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "..");
const dashboardUrl = `file://${path.join(root, "web-dashboard", "index.html")}`;
const outputDir = path.join(root, "screenshots");

const captures = [
  ["dashboard.png", "#dashboard"],
  ["request-list.png", "#requests"],
  ["request-detail.png", ".detail"],
  ["batch-result.png", "#batch"],
  ["logs-or-tests.png", "#logs"]
];

const browser = await chromium.launch();
const page = await browser.newPage({ viewport: { width: 1440, height: 980 }, deviceScaleFactor: 1 });
await page.goto(dashboardUrl);
for (const [fileName, selector] of captures) {
  const locator = page.locator(selector);
  await locator.screenshot({ path: path.join(outputDir, fileName) });
}
await browser.close();
console.log(`Generated ${captures.length} screenshots in ${outputDir}`);

