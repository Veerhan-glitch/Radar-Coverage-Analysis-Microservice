document.addEventListener("DOMContentLoaded", () => {
  const runBtn = document.querySelector(".run-btn");
  const map3D = document.querySelector(".map-placeholder");
  const chartsSection = document.querySelector(".charts-placeholder");
  const logBox = document.querySelector(".log-box");
  const statusText = document.querySelector(".footer .status p");
  const progressBar = document.querySelector("progress");
  const inputs = document.querySelectorAll("aside input:not([type='file']), aside select");
  const navLinks = document.querySelectorAll(".nav-menu a");

  navLinks.forEach(link => {
    link.addEventListener("click", e => {
      e.preventDefault();
      alert(`Navigation to "${link.textContent}" page is simulated.`);
    });
  });

  runBtn.addEventListener("click", async () => {
    if (!validateInputs()) {
      alert("Please fill in all radar parameters before running the simulation.");
      return;
    }

    log("Starting simulation...");
    statusText.textContent = "Status: Running";
    progressBar.value = 10;

    try {
      log("Fetching radar power data from API...");
      const inputData = {};
      inputs.forEach(input => {
        const name = input.name || input.id;
        if (name) inputData[name] = parseFloat(input.value);
      });
      console.log(inputData);

      const powerRes = await fetch("http://localhost:8080/api/radar/power", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(inputData)
      });
      const powerData = await powerRes.json();

      progressBar.value = 40;

      log("Fetching radar coverage data from API...");
      const coverageRes = await fetch("http://localhost:8080/api/radar/coverage", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(inputData)
      });
      const coverageData = await coverageRes.json();

      progressBar.value = 70;

      window.powerData = powerData;
      window.coverageData = coverageData;

      log("Rendering visualizations...");
      progressBar.value = 100;
      statusText.textContent = "Status: Complete";
      log("Simulation complete.");

      render3DMap(map3D, inputData.rcs);
      renderCharts(chartsSection, powerData);

      const summary = document.getElementById("summaryText");
      if (summary) {
        summary.innerText = `Max Range: ${(coverageData.maxRange / 1000).toFixed(2)} km | Min Detectable Signal: ${powerData.minDetectableSignalDb.toFixed(2)} dB`;
      }
    } catch (e) {
      log("Simulation failed: " + e.message);
      statusText.textContent = "Status: Failed";
      progressBar.value = 0;
    }
  });

  function validateInputs() {
    let valid = true;
    inputs.forEach(input => {
      if (input.value.trim() === "") {
        input.style.border = "2px solid red";
        valid = false;
      } else {
        input.style.border = "";
      }
    });
    return valid;
  }

  function log(message) {
    const p = document.createElement("p");
    p.textContent = `[${new Date().toLocaleTimeString()}] ${message}`;
    logBox.appendChild(p);
    logBox.scrollTop = logBox.scrollHeight;
  }
});

function render3DMap(container, radiusMeters = 100000) {
  container.innerHTML = "";

const viewer = new Cesium.Viewer(container, {
  fullscreenElement: container, // 👈 THIS is the key line

    terrainProvider: new Cesium.EllipsoidTerrainProvider(),
    geocoder: new CustomLocalGeocoder(),
    animation: false,
    timeline: false,
    shouldAnimate: false
  });

  const handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
  handler.setInputAction((movement) => {
    const pickedPosition = viewer.scene.pickPosition(movement.position);
    if (!pickedPosition) return;

    const cartographic = Cesium.Cartographic.fromCartesian(pickedPosition);
    const lat = Cesium.Math.toDegrees(cartographic.latitude).toFixed(4);
    const lon = Cesium.Math.toDegrees(cartographic.longitude).toFixed(4);

    viewer.entities.removeAll();

    viewer.entities.add({
      name: "Dome Structure",
      position: pickedPosition,
      ellipsoid: {
        radii: new Cesium.Cartesian3(radiusMeters, radiusMeters, radiusMeters / 2),
        material: Cesium.Color.RED.withAlpha(0.5),
      },
      description: `<b>Dome Structure</b><br>Lat: ${lat}°<br>Lng: ${lon}°<br>Radius: ${(radiusMeters / 1000).toFixed(2)} km`
    });

    viewer.selectedEntity = viewer.entities.values[0];
  }, Cesium.ScreenSpaceEventType.LEFT_CLICK);
}

function CustomLocalGeocoder() {
  this.name = "Offline Geocoder";
}
CustomLocalGeocoder.prototype.geocode = function (input) {
  const locations = {
    "delhi": { lat: 28.6139, lon: 77.2090 },
    "mumbai": { lat: 19.0760, lon: 72.8777 },
    "kolkata": { lat: 22.5726, lon: 88.3639 },
    "bengaluru": { lat: 12.9716, lon: 77.5946 },
    "chennai": { lat: 13.0827, lon: 80.2707 },
    "hyderabad": { lat: 17.3850, lon: 78.4867 },
    "pune": { lat: 18.5204, lon: 73.8567 },
    "ahmedabad": { lat: 23.0225, lon: 72.5714 },
    "jaipur": { lat: 26.9124, lon: 75.7873 },
    "lucknow": { lat: 26.8467, lon: 80.9462 }
  };

  const key = input.trim().toLowerCase();
  if (locations[key]) {
    const coords = locations[key];
    return Promise.resolve([{
      displayName: key.charAt(0).toUpperCase() + key.slice(1),
      destination: Cesium.Cartesian3.fromDegrees(coords.lon, coords.lat)
    }]);
  }
  return Promise.resolve([]);
};

function renderCharts(container, powerData) {
  container.innerHTML = "";
  const canvas = document.createElement("canvas");
  canvas.id = "signalChart";
  canvas.width = 600;
  canvas.height = 300;
  container.appendChild(canvas);

  const ctx = canvas.getContext("2d");
  new Chart(ctx, {
    type: "line",
    data: {
      labels: powerData.distances.map(d => (d / 1000).toFixed(1)),
      datasets: [
        {
          label: "Signal Strength (dB)",
          data: powerData.receivedPowerDb.map(d => d.toFixed(2)),
          borderColor: "blue",
          fill: false,
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Received Power vs Distance",
        },
      },
      scales: {
        x: {
          title: { display: true, text: "Distance (km)" }
        },
        y: {
          title: { display: true, text: "Power (dB)" }
        }
      }
    },
  });
}

function exportToPDF() {
  const { jsPDF } = window.jspdf;
  const doc = new jsPDF();

  const params = [
    ["Frequency Band", document.querySelector("aside select")?.value],
    ["Transmit Power (kW)", document.querySelectorAll("aside input[type='number']")[0]?.value],
    ["Antenna Gain (dBi)", document.querySelectorAll("aside input[type='number']")[1]?.value],
    ["Radar Cross Section (m²)", document.querySelectorAll("aside input[type='number']")[2]?.value],
    ["Atmospheric Profile", document.querySelectorAll("aside input[type='file']")[0]?.files[0]?.name || "None"],
    ["Terrain Data (DTED)", document.querySelectorAll("aside input[type='file']")[1]?.files[0]?.name || "None"],
    ["Refractivity Model", document.querySelectorAll("aside select")[1]?.value],
    ["Simulation Range (km)", document.querySelector("aside input[type='range']")?.value],
    ["Horizontal Resolution", document.querySelectorAll("aside input[type='number']")[3]?.value],
    ["Vertical Resolution", document.querySelectorAll("aside input[type='number']")[4]?.value],
  ];

  doc.setFontSize(16);
  doc.text("Radar Coverage Simulator Report", 10, 15);
  doc.setFontSize(13);
  doc.text("Simulation Parameters:", 10, 25);

  let y = 32;
  params.forEach(([label, value]) => {
    doc.text(`${label}:`, 12, y);
    doc.text(String(value || "N/A"), 80, y);
    y += 7;
  });

  const summary = document.querySelector("#summaryText")?.textContent || "No summary available.";
  doc.setFontSize(12);
  doc.text("Simulation Summary:", 10, y + 5);
  doc.text(doc.splitTextToSize(summary, 180), 10, y + 12);

  const logs = Array.from(document.querySelectorAll(".log-box p"))
    .map((p) => p.textContent)
    .join("\n");
  const splitLogs = doc.splitTextToSize(logs, 180);

  doc.addPage();
  doc.setFontSize(13);
  doc.text("Log Messages:", 10, 15);
  doc.setFontSize(11);
  doc.text(splitLogs, 10, 25);

  const canvas = document.getElementById("signalChart");
  if (canvas) {
    const imgData = canvas.toDataURL("image/png");
    doc.addPage();
    doc.setFontSize(14);
    doc.text("Signal Chart", 10, 15);
    doc.addImage(imgData, "PNG", 10, 20, 180, 100);
  }

  doc.save("simulation_report.pdf");
}

function exportToCSV() {
  const rows = [["Distance (km)", "Signal Strength (dB)"]];
  const chart = Chart.getChart("signalChart");
  if (!chart) {
    alert("Chart not found. Run the simulation first.");
    return;
  }

  const labels = chart.data.labels;
  const signalData = chart.data.datasets[0].data;

  for (let i = 0; i < labels.length; i++) {
    rows.push([labels[i], signalData[i]]);
  }

  const csvContent = "data:text/csv;charset=utf-8," + rows.map((r) => r.join(",")).join("\n");
  const encodedUri = encodeURI(csvContent);
  const link = document.createElement("a");
  link.setAttribute("href", encodedUri);
  link.setAttribute("download", "simulation_data.csv");
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

