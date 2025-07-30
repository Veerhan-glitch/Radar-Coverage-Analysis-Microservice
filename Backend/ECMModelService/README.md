## ECMModelService

### Purpose

This microservice calculates **Jamming-to-Radar Advantage (J/R Advantage)** based on various ECM (Electronic Countermeasure) parameters. It is used by the frontend dashboard to evaluate ECM effectiveness in radar environments.

---

### Endpoint

`/api/ecm/calculate` (POST)

---

### Input

```json
{
  "pJ": ...,
  "gJ": ...,
  "lJ": ...,
  "azimuthJ": ...,
  "elevationJ": ...,
  "beamwidthAzJ": ...,
  "beamwidthElJ": ...,
  "bJ": ...,
  "freqJ": ...,
  "latJ": ...,
  "longJ": ...,
  "heightJ": ...,
  "jammerType": ...,
  "latR": ...,
  "longR": ...,
  "heightR": ...,
  "bR": ...,
  "freqR": ...
}
```

---

### Field Descriptions

#### Jammer Parameters

* `pJ`: Power of jammer (watts)
* `gJ`: Gain of jammer antenna (dB)
* `lJ`: Loss in jammer system (dB)
* `azimuthJ`, `elevationJ`: Orientation angles (degrees)
* `beamwidthAzJ`, `beamwidthElJ`: Beamwidth of jammer in azimuth/elevation (degrees)
* `bJ`: Bandwidth of jammer (Hz)
* `freqJ`: Jammer frequency (GHz)
* `latJ`, `longJ`, `heightJ`: Jammer location (degrees/meters)
* `jammerType`: Type of jammer (e.g., "noise", "DRFM")

#### Radar Parameters

* `latR`, `longR`, `heightR`: Radar location (degrees/meters)
* `bR`: Radar receiver bandwidth (Hz)
* `freqR`: Radar frequency (GHz)

---

### Output

```json
{
  "id": ...,
  "jRa": ...
}
```
* `id`: Output record ID (auto-generated)
* `jRa`: Computed Jamming-to-Radar Advantage in dB

---

### File Roles & Flow

* **`Application.java`**
  Entry point for the Spring Boot application.

* **`EcmController.java`**
  REST controller exposing the `/api/ecm/calculate` POST endpoint.
  It receives ECM parameters and returns the calculated output.

* **`EcmService.java`**
  Contains the main logic to:

  * Store input parameters
  * Calculate 3D distance
  * Apply signal loss and gain formulas
  * Save and return the result

* **`ECMParameters.java`**
  JPA entity class that maps to the input parameters table.

* **`ECMOutputs.java`**
  JPA entity class that maps to the output results table (J/R Advantage).

* **`ECMParametersRepository.java` & `ECMOutputsRepository.java`**
  Spring Data JPA repositories for database operations.

---

### Database Storage

* **Table:** `ecmparameters`  
* **Stored Fields:**

  * `id` (Long, auto-generated)
  * `pJ` (double, required)
  * `gJ` (double, required)
  * `lJ` (double, required)
  * `azimuthJ` (double, required)
  * `elevationJ` (double, required)
  * `beamwidthAzJ` (double, required)
  * `beamwidthElJ` (double, required)
  * `bJ` (double, required)
  * `freqJ` (double, required)
  * `latJ` (double, required)
  * `longJ` (double, required)
  * `heightJ` (double, required)
  * `jammerType` (String, required)
  * `latR` (double, required)
  * `longR` (double, required)
  * `heightR` (double, required)
  * `bR` (double, required)
  * `freqR` (double, required)

---

* **Table:** `ecmoutputs`  
* **Stored Fields:**

  * `id` (Long, auto-generated)
  * `jRa` (double, calculated)
  * `parameter_id` (Long, required, foreign key to `ecmparameters.id`)

---

### Summary of Connections

```
AnalysisController <--> EcmController <--> EcmService <--> ECMModel
```

---