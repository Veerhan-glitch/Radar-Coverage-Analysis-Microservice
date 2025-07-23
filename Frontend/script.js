// script.js
window.CESIUM_BASE_URL = "libs/cesium";

document.addEventListener("DOMContentLoaded", () => {
  // ─── Element References ────────────────────────────────────────────────
  const serviceSelect = document.getElementById("service-select");
  const forms = {
    "absorption-loss": document.getElementById("form-absorption-loss"),
    "ecm-model":       document.getElementById("form-ecm-model"),
    "radar-model":     document.getElementById("form-radar-model")
  };
  const antennaType   = document.getElementById("antennaType");
  const antSubWrap    = document.getElementById("antennaSubWrapper");
  const radSubWrap    = document.getElementById("radarSubWrapper");
  const addWeatherBtn = document.getElementById("add-weather-btn");
  const weatherCont   = document.getElementById("weather-container");
  const runBtn        = document.querySelector(".run-btn");
  const map3D         = document.getElementById("map3D");
  const chartsSection = document.querySelector(".charts-placeholder");
  const logBox        = document.querySelector(".log-box");
  const statusText    = document.querySelector(".status p");
  const progressBar   = document.querySelector("progress");

  const serviceEndpoints = {
    "ecm-model":       "http://localhost:8082/api/ecm/calculate",
    "radar-model":     "http://localhost:8083/api/radar/calculate",
    "absorption-loss": "http://localhost:8081/api/absorption/calculate",
  };

  // ─── Initialization ────────────────────────────────────────────────────
  antennaType  .addEventListener("change", () => {
    antSubWrap.style.display = antennaType.value === "Gain Pattern" ? "flex" : "none";
  });

  addWeatherBtn.addEventListener("click", () => addWeatherBlock(weatherCont));
  runBtn.addEventListener("click", callMicroservice);
  serviceSelect.addEventListener("change", onServiceChange);

  // Trigger initial form setup
    serviceSelect.value = "absorption-loss";
  onServiceChange();

  // ─── Show/hide service forms & reset weather blocks ────────────────────
  function onServiceChange() {
    const svc = serviceSelect.value;
    // hide all
    Object.values(forms).forEach(div => div.style.display = "none");
    // show selected
    forms[svc].style.display = "block";

    // sub‑dropdown visibility
    antSubWrap.style.display = (svc === "ecm-model" && antennaType.value === "A") ? "flex" : "none";
    radSubWrap.style.display = (svc === "radar-model" && radarType.value === "X")  ? "flex" : "none";

    // weather UI only for absorption‑loss
    addWeatherBtn.style.display = svc === "absorption-loss" ? "block" : "none";
if (svc !== "absorption-loss") {
  weatherCont.innerHTML = "";
} else {
  weatherCont.innerHTML = "";
  addWeatherBlock(weatherCont, true);  // <-- pass true to add default
}
  }

  // ─── Create one weather block ──────────────────────────────────────────
function addWeatherBlock(container, isDefault = false) {
  const block = document.createElement("div");
  block.className = "weather-block";
  block.innerHTML = `
    <label>Weather Type:
      <select name="weatherType">
        <option value="clear">clear</option>
        <option value="rain">rain</option>
        <option value="snow">snow</option>
        <option value="fog">fog</option>
      </select>
    </label>
    <label>
      <input type="checkbox" name="wholeGlobe"> Whole Globe
    </label>
    <div class="coords">
      <label>Lat Min: <input type="number" name="latMin"></label>
      <label>Lat Max: <input type="number" name="latMax"></label>
      <label>Long Min: <input type="number" name="longMin"></label>
      <label>Long Max: <input type="number" name="longMax"></label>
      <label>Height Min: <input type="number" name="heightMin"></label>
      <label>Height Max: <input type="number" name="heightMax"></label>
    </div>
    <button type="button" class="remove-weather">Remove</button>
  `;

  const chk = block.querySelector("input[name=wholeGlobe]");
  const coords = block.querySelector(".coords");

  chk.addEventListener("change", () => {
    coords.style.display = chk.checked ? "none" : "block";
  });

  // Remove button handler
  block.querySelector(".remove-weather")
       .addEventListener("click", () => block.remove());

  // Set default values if needed
  if (isDefault) {
    block.querySelector("select[name=weatherType]").value = "clear";
    chk.checked = true;
    coords.style.display = "none";
  }

  container.appendChild(block);
}


  // ─── Collect inputs & call the correct microservice ───────────────────
  async function callMicroservice() {
    const svc = serviceSelect.value;
    const formDiv = forms[svc];

    // gather all <input> and <select> within the active form
    const entries = Array.from(formDiv.querySelectorAll("input,select"))
      .filter(el => el.id || el.name)
      .map(el => {
        // skip the remove buttons
        if (el.type === "button") return null;
        const key = el.id || el.name;
        const val = el.type === "number" ? +el.value : el.value;
        return [key, val];
      })
      .filter(Boolean);

    // add weatherConditions array for absorption-loss
    if (svc === "absorption-loss") {
      const weatherData = [];
      weatherCont.querySelectorAll(".weather-block").forEach(block => {
        const obj = {
          weatherType: block.querySelector("select[name=weatherType]").value,
          wholeGlobe: block.querySelector("input[name=wholeGlobe]").checked
        };
        if (!obj.wholeGlobe) {
          ["latMin","latMax","longMin","longMax","heightMin","heightMax"]
            .forEach(name => {
              obj[name] = +block.querySelector(`input[name=${name}]`).value;
            });
        }
        weatherData.push(obj);
      });
      entries.push(["weatherConditions", weatherData]);
    }

    const data = Object.fromEntries(entries);
console.log("Collected fields:", data);  // or the object you're about to send

    // UI feedback
    logBox.innerHTML += `<p>[${new Date().toLocaleTimeString()}] Calling ${svc}…</p>`;
    statusText.textContent = "Status: Running";
    progressBar.value = 30;

    try {
      const res = await fetch(serviceEndpoints[svc], {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify(data)
      });
      const result = await res.json();
      statusText.textContent = "Status: Completed";
      progressBar.value = 100;
      renderResults(result, data);
      renderMap(data);
      updateSummary(svc);
    } catch (err) {
      console.error(err);
      statusText.textContent = "Status: Failed";
      progressBar.value = 0;
    }
  }
function renderResults(result) {
  chartsSection.innerHTML = "";

  const container = document.createElement("div");
  container.classList.add("vertical-result");

  const tableStyle = "border-collapse: collapse; margin-bottom: 10px; width: 100%;";
  const cellStyle = "border: 1px solid #333; padding: 4px 8px;";

  const outputTable = document.createElement("table");
  outputTable.setAttribute("style", tableStyle);

  Object.entries(result).forEach(([key, value]) => {
    if (["id", "parameters", "ldashDb", "ldashDbLog"].includes(key)) return;

    const row = document.createElement("tr");

    const keyCell = document.createElement("td");
    keyCell.setAttribute("style", cellStyle);
    keyCell.textContent = key;

    const valueCell = document.createElement("td");
    valueCell.setAttribute("style", cellStyle);
    valueCell.textContent = typeof value === "number" ? value.toFixed(4) : value;

    row.appendChild(keyCell);
    row.appendChild(valueCell);
    outputTable.appendChild(row);
  });

  container.appendChild(outputTable);
  chartsSection.appendChild(container);
}





  // ─── Render Cesium entities ───────────────────────────────────────────
  function renderMap(inputData) {
    map3D.innerHTML = "";
    const viewer = new Cesium.Viewer(map3D, {
      terrainProvider: new Cesium.EllipsoidTerrainProvider(),
      sceneModePicker: false,
      timeline: false,
      animation: false,
      fullscreenElement: map3D
    });

    const add = (name,lon,lat,h,color) => {
      if (lon==null||lat==null) return;
      viewer.entities.add({
        name,
        position: Cesium.Cartesian3.fromDegrees(+lon,+lat,+h||0),
        point: { pixelSize:10, color }
      });
    };

    add("Target", inputData.longT, inputData.latT, inputData.heightT, Cesium.Color.RED);
    add("Radar",  inputData.longR, inputData.latR, inputData.heightR, Cesium.Color.BLUE);
    add("Jammer", inputData.longJ, inputData.latJ, inputData.heightJ, Cesium.Color.YELLOW);

    if (viewer.entities.values.length) viewer.zoomTo(viewer.entities);
  }

  // ─── Summary & Exports ────────────────────────────────────────────────
  function updateSummary(svc) {
    const m = {
      "ecm-model":       "ECM Model Simulation Output",
      "radar-model":     "Radar Model Simulation Output",
      "absorption-loss": "Atmospheric Absorption Simulation Output"
    };
    document.getElementById("summaryText").textContent = m[svc]||"Summary";
  }
  window.exportToPDF = function(){ /* your existing code */ };
  window.exportToCSV = function(){ /* your existing code */ };
});
