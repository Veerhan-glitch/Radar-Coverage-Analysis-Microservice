## AbsorptionLossService

### Purpose

This microservice calculates **atmospheric absorption loss** based on weather and terrain input parameters. It is consumed by the frontend dashboard for modeling radar propagation.

---

### Endpoint

/api/absorption/calculate  (POST)

### Input

```json
{
  "rfr": ...,
  "sfr": ...,
  "visFog": ...,
  "freqR": ...,
  "freqJ": ...,
  "freqOp": ...,
  "latR": ...,
  "longR": ...,
  "heightR": ...,
  "latT": ...,
  "longT": ...,
  "heightT": ...,
  "latJ": ...,
  "longJ": ...,
  "heightJ": ...,
  "weatherConditions": [
    {
      "weatherType": ...,
      "wholeGlobe": false,
      "lat1": ..,
      "lat2": ...,
      "lat3": ...,
      "lat4": ...,
      "long1": ...,
      "long2": ...,
      "long3": ...,
      "long4": ...,
      "height": ...
    },
    {
      "weatherType": ...,
      "wholeGlobe": true,
      "lat1": null,
      "lat2": null,
      "lat3": null,
      "lat4": null,
      "long1": null,
      "long2": null,
      "long3": null,
      "long4": null,
      "height": ...
    }
  ]
}


```

---

### Field Descriptions

#### Main Parameters
- `rfr`: Rainfall rate (in mm/hr)
- `sfr`: Snowfall rate (unit depends on context; could be mm/hr or cm/hr)
- `visFog`: Visibility in fog (in meters)
- `freqR`: Receiver frequency (in GHz or MHz, depending on system)
- `freqJ`: Jammer frequency (in GHz or MHz)
- `freqOp`: Operating frequency used in absorption calculations (in GHz or MHz)

#### Receiver Location
- `latR`: Latitude of the receiver (in degrees)
- `longR`: Longitude of the receiver (in degrees)
- `heightR`: Height of the receiver (in meters)

#### Transmitter Location
- `latT`: Latitude of the transmitter (in degrees)
- `longT`: Longitude of the transmitter (in degrees)
- `heightT`: Height of the transmitter (in meters)

#### Jammer Location
- `latJ`: Latitude of the jammer (in degrees)
- `longJ`: Longitude of the jammer (in degrees)
- `heightJ`: Height of the jammer (in meters)

---

### Weather Conditions (List)
Each item in the `weatherConditions` array represents a different weather zone or condition.

- `weatherType`: Type of weather (e.g., `"Rain"`, `"Fog"`, `"Snow"`)
- `wholeGlobe`: Boolean indicating if the weather condition affects the entire globe
- `lat1` to `lat4`: Latitude corners defining a bounding box for the weather zone (in degrees)
- `long1` to `long4`: Longitude corners defining a bounding box for the weather zone (in degrees)
- `height`: Altitude of the weather zone (in meters)

If `wholeGlobe` is `true`, all `latX` and `longX` fields are expected to be `null`.

---

### Output

```json
{ "id".., "ldash": ... }
```

* `id`:  Output record ID (auto-generated)
* `ldash`: Computed atmospheric absorption loss in dB


---

### File Roles & Flow

* **`AbsorptionLossController.java`**

  * Defines the `/api/absorption/calculate` POST endpoint.
  * Accepts validated JSON input and returns formatted output.
  * Calls `AbsorptionLossService`.

* **`AbsorptionLossService.java`**

  * Contains core logic to calculate `ldash` (absorption loss) based on input.
  * Delegates mathematical modeling logic to `AbsorptionLossModel`.

* **`AbsorptionLossModel.java`**

  * Contains the actual scientific formulas for computing loss based on the given atmospheric data.

* **`AbsorptionLossRequest.java`**

  * DTO class for deserializing incoming JSON data.
  * Includes fields for all required parameters.

* **`AbsorptionLossResponse.java`**

  * DTO for structuring the API response in the format `{ "absorptionOutput": { "ldash": value } }`.

---

### Database Storage

* **Table:** `atmospheric_parameters`
* **Stored Fields:**

  * `id` (Long, auto-generated)
  * `rfr` (double, required)
  * `sfr` (double, required)
  * `visFog` (double, required)
  * `freqR` (double, required)
  * `freqJ` (double, required)
  * `freqOp` (double, required)
  * `latR` (double, required)
  * `longR` (double, required)
  * `heightR` (double, required)
  * `latT` (double, required)
  * `longT` (double, required)
  * `heightT` (double, required)
  * `latJ` (double, required)
  * `longJ` (double, required)
  * `heightJ` (double, required)

---

* **Table:** `atmospheric_outputs`
* **Stored Fields:**

  * `id` (Long, auto-generated)
  * `lDash` (double, calculated)
  * `parameter_id` (Long, required, foreign key to `atmospheric_parameters.id`)

---

* **Table:** `weather_condition`
* **Stored Fields:**

  * `id` (Long, auto-generated)
  * `weatherType` (String, required)
  * `wholeGlobe` (boolean, required)
  * `lat1` (Double, optional)
  * `lat2` (Double, optional)
  * `lat3` (Double, optional)
  * `lat4` (Double, optional)
  * `long1` (Double, optional)
  * `long2` (Double, optional)
  * `long3` (Double, optional)
  * `long4` (Double, optional)
  * `height` (Double, optional)
  * `parameters_id` (Long, required, foreign key to `atmospheric_parameters.id`)

---

### Summary of Connections

```
AnalysisController <--> AbsorptionLossController <--> AbsorptionLossService <--> AbsorptionLossModel
```

---