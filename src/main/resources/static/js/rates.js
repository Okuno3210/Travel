// /js/rates.js
const FxRate = (function () {

  // 国コード → 通貨コード
  const countryToCurrency = {
    USA: "USD", ITA: "EUR", FRA: "EUR", VNM: "VND", CHE: "CHF",
    AUS: "AUD", THA: "THB", EGY: "EGP", IRN: "IRR", IND: "INR",
    CHN: "CNY", RUS: "RUB", KOR: "KRW",
  };

  // currency-api（@fawazahmed0）を利用
  const API_BASE = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1";

  /** 通貨レート取得 */
  async function fetchRate(from, to) {
    const res = await fetch(`${API_BASE}/currencies/${from.toLowerCase()}.json`);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);

    const data = await res.json();
    const rate = data[from.toLowerCase()][to.toLowerCase()];
    if (!rate) throw new Error("通貨データが見つかりません");

    return { rate, date: data.date };
  }

  /** 表示更新 */
  async function update(containerId, countryCode, to = "JPY") {
    const container = document.getElementById(containerId);
    if (!container) return;

    const base = countryToCurrency[countryCode] || "USD";
    container.textContent = `${base}/${to}: 読み込み中…`;

    try {
      const { rate, date } = await fetchRate(base, to);
      container.textContent = `${base}/${to}: ${rate.toFixed(3)}（${date}）`;
    } catch (err) {
      container.textContent = `${base}/${to}: エラー (${err.message})`;
      console.warn(err);
    }
  }

  /** 自動更新開始 */
  function start(containerId, countryCode, intervalMs = 30000) {
    update(containerId, countryCode);
    setInterval(() => update(containerId, countryCode), intervalMs);

    insertCredit(); // ← 出典クレジットを挿入
  }

  /** 出典表記を自動挿入 */
  function insertCredit() {
    if (document.getElementById("fxrate-credit")) return;

    const credit = document.createElement("p");
    credit.id = "fxrate-credit";
    credit.style.cssText = "text-align:center; font-size:0.85em; color:#666; margin-top:20px;";
    credit.innerHTML =
      '為替レート提供：<a href="https://github.com/fawazahmed0/exchange-api" target="_blank">@fawazahmed0/currency-api</a>';

    // footerの直前に挿入
    const footer = document.querySelector("footer, [th\\:replace*='footer']");
    if (footer) {
      footer.parentNode.insertBefore(credit, footer);
    } else {
      document.body.appendChild(credit);
    }
  }


  return { start };

})();
