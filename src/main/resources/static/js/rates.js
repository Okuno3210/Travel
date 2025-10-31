// script/fxRate.js
const FxRate = (function () {

  const pairs = [
    ["USD", "JPY"],
  ];

  const API_BASE = "https://api.frankfurter.app/latest";

  async function fetchRate(from, to) {
    const res = await fetch(`${API_BASE}?from=${from}&to=${to}`);
    if (!res.ok) throw new Error(res.statusText);
    const data = await res.json();
    return { rate: data.rates[to], date: data.date, base: data.base };
  }

  async function update(containerId) {
    const container = document.getElementById(containerId);
    if (!container) return console.warn(`Container #${containerId} not found`);
    container.innerHTML = "";

    for (const [from, to] of pairs) {
      const div = document.createElement("div");
      div.textContent = `${from}/${to}: 読み込み中…`;
      container.appendChild(div);

      try {
        const { rate, date } = await fetchRate(from, to);
        div.textContent = `${from}/${to}: ${rate.toFixed(3)}（${date}）`;
      } catch (err) {
        div.textContent = `${from}/${to}: エラー (${err.message})`;
      }
    }
  }

  function start(containerId, intervalMs = 60000) {
    update(containerId);
    setInterval(() => update(containerId), intervalMs);
  }

  // 外部から使える関数を公開
  return { start };

})();
