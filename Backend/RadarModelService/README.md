## RadarModelService

### Purpose

This microservice calculates **radar signal performance metrics** such as received power, signal-to-interference ratio (SIR), and probability of detection (Pd), based on radar and target configuration inputs.

---

### Endpoint

**POST** `/api/radar/calculate`

---

### Input

```json
{
  "gr": ...,
  "lo": ...,
  "freqR": ...,
  "pr": ...,
  "br": ...,
  "fr": ...,
  "k": ...,
  "t": ...,
  "latT": ...,
  "longT": ...,
  "heightT": ...,
  "pfa": ...
}
```

#### Field Descriptions

* `gr`: Radar gain (dB)
* `lo`: System loss (dB)
* `freqR`: Radar frequency (GHz)
* `pr`: Transmit power (W)
* `br`: Receiver bandwidth (MHz)
* `fr`: Noise figure (dB)
* `k`: Boltzmann constant (J/K)
* `t`: System temperature (K)
* `latT`, `longT`, `heightT`: Target location (latitude, longitude in degrees, height in meters)
* `pfa`: Probability of false alarm (between 0 and 1)

---

### Output

```json
{
  "id": ...,
  "sReceiveRadar": ...,
  "sIRadar": ...,
  "pd": ...
}
```

#### Output Fields

* `id`: Output record ID (auto-generated)
* `sReceiveRadar`: Received signal power at radar (in dB)
* `sIRadar`: Signal-to-interference ratio (in dB)
* `pd`: Probability of detection (unitless, between 0 and 1)

---

### File Roles & Flow

#### Controller

* `RadarController.java`
  Defines the `/api/radar/calculate` endpoint. Accepts a `RadarParameters` object and returns `RadarOutputs`.

#### Service

* `RadarService.java`
  Core logic for radar equation computation, noise calculation, and Pd estimation.

#### Entities

* `RadarParameters.java`
  Input parameters for radar performance calculations.

* `RadarOutputs.java`
  Output results including received power, SIR, and Pd.

#### Repositories

* `RadarParametersRepository.java`
  JPA repository for persisting input parameters.

* `RadarOutputsRepository.java`
  JPA repository for persisting radar calculation results.

---

### Database Storage

* **Table:** `radar_parameters`  
* **Stored Fields:**

  * `id` (Long, auto-generated)  
  * `gr` (double, required)  
  * `lo` (double, required)  
  * `freqR` (double, required)  
  * `pr` (double, required)  
  * `br` (double, required)  
  * `fr` (double, required)  
  * `k` (double, required)  
  * `t` (double, required)  
  * `latT` (double, required)  
  * `longT` (double, required)  
  * `heightT` (double, required)  
  * `pfa` (double, required)

---

* **Table:** `radar_outputs`  
* **Stored Fields:**

  * `id` (Long, auto-generated)  
  * `sReceiveRadar` (double, calculated)  
  * `sIRadar` (double, calculated)  
  * `pd` (double, calculated)  
  * `parameter_id` (Long, required, foreign key to `radar_parameters.id`)

---

### Summary of Connections

```
AnalysisController <--> RadarController <--> RadarService <--> RadarModel
```