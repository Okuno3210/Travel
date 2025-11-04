// /js/rates.js
const FxRate = (function () {

  const countryToCurrency = {
    USA: "USD",
    ITA: "EUR",
    FRA: "EUR",
    VNM: "VND",
    CHE: "CHF",
    AUS: "AUD",
    THA: "THB",
    EGY: "EGP",
    IRN: "IRR",
    IND: "INR",
    CHN: "CNY",
    RUS: "RUB",
    KOR: "KRW",
  };

  // ✅ exchangerate.host に統一
  const API_BASE = "https://api.exchangerate.host/latest";

  async function fetchRate(from, to) {
    const res = await fetch(`${API_BASE}?base=${from}&symbols=${to}`);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);

    const data = await res.json();

    // レスポンス確認
    if (!data || !data.rates || !data.rates[to]) {
      // error がオブジェクトのことがあるので、文字列化
      const msg =
        typeof data.error === "string"
          ? data.error
          : data.error
          ? JSON.stringify(data.error)
          : "レート情報なし";
      throw new Error(msg);
    }

    return { rate: data.rates[to], date: data.date };
  }


  async function update(containerId, countryCode, to = "JPY") {
    const container = document.getElementById(containerId);
    if (!container) return console.warn(`Container #${containerId} not found`);

    const base = countryToCurrency[countryCode] || "USD";
    container.textContent = `${base}/${to}: 読み込み中…`;

    try {
      const { rate, date } = await fetchRate(base, to);
      container.textContent = `${base}/${to}: ${rate.toFixed(3)}（${date}）`;
    } catch (err) {
      container.textContent = `${base}/${to}: エラー (${err.message})`;
      console.warn(`FX取得失敗: ${base}/${to}`, err);
    }
  }

  function start(containerId, countryCode, intervalMs = 60000) {
    update(containerId, countryCode);
    setInterval(() => update(containerId, countryCode), intervalMs);
  }

  return { start };
})();
