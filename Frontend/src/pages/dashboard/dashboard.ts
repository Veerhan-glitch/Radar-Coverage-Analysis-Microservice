// Angular imports
import {
  Component,
  AfterViewInit,
  ViewChild,
  ElementRef,
  ViewEncapsulation
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

// External libraries
declare const Cesium: any;           // For 3D map rendering
import { jsPDF } from 'jspdf';       // For PDF export
import { errorContext } from 'rxjs/internal/util/errorContext';
declare const Chart: any;            // For charting (if used)

@Component({
  selector: 'app-dashboard',
  standalone: true,  // Enables this component to be used without NgModule
  imports: [CommonModule, FormsModule, HttpClientModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'],
  encapsulation: ViewEncapsulation.None  // Allows global CSS styles
})

export class Dashboard implements AfterViewInit {
  // Form element references (linked to HTML template using @ViewChild)
  @ViewChild('serviceSelect') serviceSelect!: ElementRef<HTMLSelectElement>;       // Dropdown to choose service
  @ViewChild('addWeatherBtn') addWeatherBtn!: ElementRef<HTMLButtonElement>;       // Button to add weather input
  @ViewChild('weatherContainer') weatherContainer!: ElementRef<HTMLDivElement>;    // Container holding weather blocks
  @ViewChild('runBtn') runBtn!: ElementRef<HTMLButtonElement>;                     // Button to execute microservice
  @ViewChild('map3D') map3D!: ElementRef<HTMLDivElement>;                          // 3D map display area
  @ViewChild('chartsPlaceholder') chartsPlaceholder!: ElementRef<HTMLDivElement>;  // Placeholder for result tables/charts
  @ViewChild('logBox') logBox!: ElementRef<HTMLDivElement>;                        // Console-style log output box
  @ViewChild('statusText') statusText!: ElementRef<HTMLParagraphElement>;          // Status display text
  @ViewChild('progressBar') progressBar!: ElementRef<HTMLProgressElement>;         // Progress bar

  // Antenna dropdowns and sub-sections (for ECM and Radar)
  @ViewChild('antennaTypeECM') antennaTypeECM!: ElementRef<HTMLSelectElement>;
  @ViewChild('antennaSubWrapperECM') antennaSubECM!: ElementRef<HTMLElement>;
  @ViewChild('antennaTypeRadar') antennaTypeRadar!: ElementRef<HTMLSelectElement>;
  @ViewChild('antennaSubWrapperRadar') antennaSubRadar!: ElementRef<HTMLElement>;

  // To store input/output for each session run (used for exporting results later)
  private sessionRuns: {
    model: string;
    input: any;
    output: any;
  }[] = [];

  // API endpoints for each microservice model
  serviceEndpoints = {
    'absorption-loss': 'http://localhost:8084/api/analysis/request',
    'ecm-model':       'http://localhost:8084/api/analysis/request',
    'radar-model':     'http://localhost:8084/api/analysis/request',
  };

ngAfterViewInit(): void {
  // Set base path for Cesium assets
  (window as any).CESIUM_BASE_URL = 'assets/cesium';

  // Attach event listeners
  this.serviceSelect.nativeElement.addEventListener('change', () => this.onServiceChange());  // Handle service switch
  this.addWeatherBtn.nativeElement.addEventListener('click', () => this.addWeatherBlock(true)); // Add weather block
  this.runBtn.nativeElement.addEventListener('click', () => this.callMicroservice());           // Run selected microservice

  // Toggle antenna sub-options when dropdown changes
  this.antennaTypeECM.nativeElement.addEventListener('change', () => this.toggleAntenna('ecm'));
  this.antennaTypeRadar.nativeElement.addEventListener('change', () => this.toggleAntenna('radar'));

  // Default selections on load
  this.serviceSelect.nativeElement.value = 'absorption-loss';
  this.onServiceChange();     // Show appropriate form
  this.toggleAntenna('ecm');  // Show/hide ECM antenna options
  this.toggleAntenna('radar');// Show/hide radar antenna options
}
private units: Record<string, Record<string, string>> = {
  'absorption-loss': {
    ldash: 'dB'
  },
  'radar-model': {
    sreceiveRadar: 'dB',
    siradar: 'dB',
    pd: '' // Probability, unitless
  },
  'ecm-model': {
    jra: 'dB'
  }
};
private INPUT_LABELS: Record<string, string> = {
  rfr: 'Rainfall Rate (RFR) [mm/hr]',
  sfr: 'Snowfall Rate (SFR) [mm/hr]',
  visFog: 'Visibility in Fog [m]',
  freqR: 'Radar Frequency (Freq_r) [Hz]',
  freqJ: 'Jammer Frequency (Freq_j) [Hz]',
  freqOp: 'Operating Frequency (Freq_op) [Hz]',
  latR: 'Radar Latitude [°]',
  longR: 'Radar Longitude [°]',
  heightR: 'Radar Height [m]',
  latT: 'Target Latitude [°]',
  longT: 'Target Longitude [°]',
  heightT: 'Target Height [m]',
  latJ: 'Jammer Latitude [°]',
  longJ: 'Jammer Longitude [°]',
  heightJ: 'Jammer Height [m]',
  pJ: 'Jammer Power (P_j) [W]',
  gJ: 'Jammer Gain (G_j) [dB]',
  lJ: 'Jammer Loss (L_j) [dB]',
  azimuthJ: 'Azimuth (Azimuth_j) [°]',
  elevationJ: 'Elevation (Elevation_j) [°]',
  beamwidthAzJ: 'Beamwidth Azimuth [°]',
  beamwidthElJ: 'Beamwidth Elevation [°]',
  bJ: 'Jammer Bandwidth (B_j) [Hz]',
  bR: 'Radar Bandwidth (B_r) [Hz]',
  freqRm: 'Radar Frequency (Freq_r) [Hz]',
  gr: 'Radar Gain (G_r) [dB]',
  lo: 'Radar Loss (L_o) [dB]',
  polarizationR: 'Radar Polarization (R)',
  polarizationJ: 'Jammer Polarization (J)',
  pr: 'Radar Power (P_r) [W]',
  fr: 'Noise Figure (F_r) [dB]',
  k: 'Boltzmann Constant (k) [J/K]',
  t: 'System Temperature (T) [K]',
  BMWEl_R: 'Beamwidth Elevation [°]',
  BMWAz_R: 'Beamwidth Azimuth [°]',
  elLimit: 'Elevation Limit [°]',
  pfa: 'False Alarm Probability (Pfa)',
};


private onServiceChange(): void {
  // Hide all service forms
  const svc = this.serviceSelect.nativeElement.value;
  document.querySelectorAll<HTMLElement>('.service-form').forEach(d => d.style.display = 'none');

  // Show selected service form
  (document.getElementById(`form-${svc}`) as HTMLElement).style.display = 'block';

  if (svc === 'absorption-loss') {
    // If absorption-loss, allow multiple weather blocks
    this.weatherContainer.nativeElement.innerHTML = '';
    this.addWeatherBlock(true); // Add default weather block
    this.addWeatherBtn.nativeElement.style.display = 'block';
  } else {
    // Other services don't need weather input
    this.weatherContainer.nativeElement.innerHTML = '';
    this.addWeatherBtn.nativeElement.style.display = 'none';
  }
}

private addWeatherBlock(isDefault = false): void {
  // Dynamically adds a weather input block to the DOM
  const container = this.weatherContainer.nativeElement;
  const block = document.createElement('div');
  block.className = 'weather-block';

  // Weather block fields
  block.innerHTML = `
    <label>Weather Type:
      <select name="weatherType">
        <option value="clear">clear</option><option value="rain">rain</option>
        <option value="snow">snow</option><option value="fog">fog</option>
      </select>
    </label>
    <label><input type="checkbox" name="wholeGlobe"> Whole Globe</label>
    <div class="coords">
      <label>Lat Min:<input name="lat1" type="number"></label>
      <label>Lat Max:<input name="lat2" type="number"></label>
      <label>Lat Min:<input name="lat3" type="number"></label>
      <label>Lat Max:<input name="lat4" type="number"></label>
      <label>Long Min:<input name="long1" type="number"></label>
      <label>Long Max:<input name="long2" type="number"></label>
      <label>Long Min:<input name="long3" type="number"></label>
      <label>Long Max:<input name="long4" type="number"></label>
      <label>Height Max:<input name="height" type="number"></label>
    </div>
    <button type="button" class="remove-weather">Remove</button>
  `;

  // Toggle coordinate fields when 'Whole Globe' is checked
  const chk = block.querySelector<HTMLInputElement>('input[name=wholeGlobe]')!;
  const coords = block.querySelector<HTMLElement>('.coords')!;
  chk.addEventListener('change', () => coords.style.display = chk.checked ? 'none' : 'grid');

  // Remove weather block when 'Remove' is clicked
  block.querySelector<HTMLButtonElement>('.remove-weather')!
    .addEventListener('click', () => block.remove());

  // Apply default settings
  if (isDefault) {
    chk.checked = true;
    coords.style.display = 'none';
  }

  container.appendChild(block); // Add to container
}

private toggleAntenna(section: 'ecm' | 'radar'): void {
  // Show antenna sub-options only if "Gain Pattern" is selected
  const type = section === 'ecm'
    ? this.antennaTypeECM.nativeElement.value
    : this.antennaTypeRadar.nativeElement.value;

  const wrapper = section === 'ecm'
    ? this.antennaSubECM.nativeElement
    : this.antennaSubRadar.nativeElement;

  wrapper.style.display = type === 'Gain Pattern' ? 'block' : 'none';
}

private async callMicroservice(): Promise<void> {
  const svc = this.serviceSelect.nativeElement.value as keyof typeof this.serviceEndpoints;
  const formDiv = document.getElementById(`form-${svc}`)!;

  // Collect input fields from the form
  const entries = Array.from(formDiv.querySelectorAll<HTMLInputElement | HTMLSelectElement>('input,select'))
    .filter(el => el.id || el.name)
    .map(el => {
      if (el.tagName === 'BUTTON') return null;
      const key = el.id || el.name!;
      const val = el instanceof HTMLInputElement && el.type === 'number'
        ? +el.value : el.value;
      return [key, val];
    })
    .filter(Boolean) as [string, any][];

  // Collect weather data if using absorption-loss model
  if (svc === 'absorption-loss') {
    const weatherData: any[] = [];
    this.weatherContainer.nativeElement.querySelectorAll<HTMLElement>('.weather-block')
      .forEach(block => {
        const obj: any = {
          weatherType: (block.querySelector('select[name=weatherType]') as HTMLSelectElement).value,
          wholeGlobe: (block.querySelector('input[name=wholeGlobe]') as HTMLInputElement).checked
        };
        if (!obj.wholeGlobe) {
          ['lat1', 'lat2', 'long1', 'long2', 'heightMin', 'height']
            .forEach(n => obj[n] = +(block.querySelector(`input[name=${n}]`) as HTMLInputElement).value);
        }
        weatherData.push(obj);
      });
    entries.push(['weatherConditions', weatherData]);
  }

  const data = Object.fromEntries(entries);

  // Log request
  this.logBox.nativeElement.innerHTML += `<p>[${new Date().toLocaleTimeString()}] Calling ${svc}…</p>`;
  this.statusText.nativeElement.textContent = 'Status: Running';
  this.progressBar.nativeElement.value = 30;

  try {
    // Send POST request
    const res = await fetch(this.serviceEndpoints[svc], {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        parameters: {
          [svc.includes('radar') ? 'radar' : svc.includes('ecm') ? 'ecm' : 'absorption']: data
        }
      })
    });

    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);

    const result = await res.json();

    // Save for exporting
    this.sessionRuns.push({ model: svc, input: data, output: result });

    // Handle model failure
    if (result?.error || result?.message?.toLowerCase().includes('model not running')) {
      this.logBox.nativeElement.innerHTML += `<p>[${new Date().toLocaleTimeString()}] Model error: not running properly.</p>`;
      this.statusText.nativeElement.textContent = 'Status: Failed';
      this.progressBar.nativeElement.value = 0;
      return;
    }

    // Success: render output
    this.statusText.nativeElement.textContent = 'Status: Completed';
    this.progressBar.nativeElement.value = 100;
    this.renderResults(result);
    this.renderMap(data);

  } catch (err: any) {
    // Handle fetch/network error
    const isConnectionRefused = err.message?.includes('ERR_CONNECTION_REFUSED') || err.message?.includes('Failed to fetch');
    this.logBox.nativeElement.innerHTML += `<p>[${new Date().toLocaleTimeString()}] ${isConnectionRefused ? 'Network error' : 'Unexpected error'} on ${svc}</p>`;
    this.statusText.nativeElement.textContent = 'Status: Failed';
    this.progressBar.nativeElement.value = 0;
  }
}
private renderResults(result: any): void {
  const container = document.createElement('div');
  container.className = 'vertical-result';
  const table = document.createElement('table');

  // Determine current model to apply units
  const currentModel = this.serviceSelect.nativeElement.value;
  const unitMap = this.units[currentModel] || {};

  Object.entries(result).forEach(([k, v]) => {
    if (['id', 'parameters', 'ldashDb', 'ldashDbLog'].includes(k)) return;

    const row = table.insertRow();
    const value = typeof v === 'number' ? v.toFixed(4) : String(v);
    const unit = unitMap[k] ? ` ${unitMap[k]}` : '';

    row.insertCell().textContent = k;
    row.insertCell().textContent = `${value}${unit}`;
  });

  this.chartsPlaceholder.nativeElement.innerHTML = '';
  this.chartsPlaceholder.nativeElement.appendChild(container).appendChild(table);
}

private renderMap(inputData: any): void {
  this.map3D.nativeElement.innerHTML = '';

  // Create Cesium viewer
  const viewer = new Cesium.Viewer(this.map3D.nativeElement, {
    terrainProvider: new Cesium.EllipsoidTerrainProvider(),
    sceneModePicker: true,
    timeline: false,
    animation: false,
    homeButton: true,
    fullscreenElement: this.map3D.nativeElement
  });

  const domes = [
    ['Target', 'longT', 'latT', 'heightT', Cesium.Color.RED],
    ['Radar', 'longR', 'latR', 'heightR', Cesium.Color.BLUE],
    ['Jammer', 'longJ', 'latJ', 'heightJ', Cesium.Color.ORANGE]
  ];

  // Helper to add 3D domes
  const addDomes = () => {
    viewer.entities.removeAll();

    domes.forEach(([name, lon, lat, h, color]) => {
      const L = +inputData[lon];
      const A = +inputData[lat];
      const H = +inputData[h];

      if (!isNaN(L) && !isNaN(A)) {
        const mode = viewer.scene.mode;
        const position = Cesium.Cartesian3.fromDegrees(L, A, H || 0);

        if (mode === Cesium.SceneMode.SCENE3D) {
          viewer.entities.add({
            name,
            position,
            ellipsoid: {
              radii: new Cesium.Cartesian3(2000.0, 2000.0, 1000.0),
              material: color.withAlpha(0.6),
              outline: false,
              heightReference: Cesium.HeightReference.RELATIVE_TO_GROUND
            }
          });
        } else {
          viewer.entities.add({
            name,
            position,
            ellipse: {
              semiMajorAxis: 2000.0,
              semiMinorAxis: 2000.0,
              material: color.withAlpha(0.6),
              height: H || 0
            }
          });
        }
      }
    });
  };

  // Initial dome render
  addDomes();

  // Zoom to view domes
  viewer.zoomTo(viewer.entities).then(() => {
    const homeCameraView = {
      destination: viewer.camera.positionWC.clone(),
      orientation: {
        heading: viewer.camera.heading,
        pitch: viewer.camera.pitch,
        roll: viewer.camera.roll
      }
    };

    // Override home button to reset to this view
    viewer.homeButton.viewModel.command.beforeExecute.addEventListener((commandInfo: { cancel: boolean; }) => {
      viewer.camera.setView(homeCameraView);
      commandInfo.cancel = true;
    });
  });

  // Re-render domes after changing scene mode (2D <-> 3D)
  viewer.scene.morphComplete.addEventListener(() => {
    addDomes();
  });
}
exportToPDF(): void {
  const doc = new jsPDF();
  let y = 10;

  this.sessionRuns.forEach((run, index) => {
    if (index > 0) {
      doc.addPage();
      y = 10;
    }

    const model = run.model || 'Unknown';

    doc.setFontSize(12);
    doc.setTextColor(40);
    doc.text(`Run ${index + 1} - Model: ${model}`, 15, y);
    y += 10;

    // Inputs
    doc.setFontSize(10);
    doc.setTextColor(0);
    doc.text('Inputs:', 15, y);
    y += 6;

    Object.entries(run.input).forEach(([key, val]) => {
      if (key === 'model') return;
      const label = this.INPUT_LABELS?.[key] || key;
      const text = `${label}: ${val}`;
      doc.text(text, 15, y);
      y += 6;
      if (y > 280) { doc.addPage(); y = 10; }
    });

    y += 4;
    doc.setTextColor(0);
    doc.text('Outputs:', 15, y);
    y += 6;

    const unitsMap = this.units?.[model] || {};
    Object.entries(run.output).forEach(([key, val]) => {
      if (['id', 'parameters', 'ldashDb', 'ldashDbLog'].includes(key)) return;
      const unit = unitsMap[key] ? ` (${unitsMap[key]})` : '';
      const text = `${key}${unit}: ${val}`;
      doc.text(text, 15, y);
      y += 6;
      if (y > 280) { doc.addPage(); y = 10; }
    });
  });

  doc.save('analysis_results.pdf');
}
exportToCSV(): void {
  const rows: string[] = ['Run No.,Type,Model,Key,Label,Value'];

  this.sessionRuns.forEach((run, index) => {
    const runNo = index + 1;
    const model = run.model || 'Unknown';

    // Inputs
    Object.entries(run.input).forEach(([key, val]) => {
      if (key === 'model') return;
      const label = this.INPUT_LABELS?.[key] || key;
      rows.push(`${runNo},Input,${model},${key},"${label}","${val}"`);
    });

    // Outputs
    Object.entries(run.output).forEach(([key, val]) => {
      if (['id', 'parameters', 'ldashDb', 'ldashDbLog'].includes(key)) return;
      const label = key; // Or use this.OUTPUT_LABELS?.[key] || key
      rows.push(`${runNo},Output,${model},${key},"${label}","${val}"`);
    });
  });

  const blob = new Blob([rows.join('\n')], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'analysis_results.csv';
  a.click();
  URL.revokeObjectURL(url);
}


}
