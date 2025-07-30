## AnalysisManagerService

### Purpose

This microservice **orchestrates computation across three domain-specific services**: Absorption Loss, ECM Model, and Radar Model. It collects inputs, sends them to the respective services via REST, and aggregates their outputs into a unified response.

---

###  Input

* **POST** `/api/analysis/request`
* **Request Body**: JSON format with exactly **one model** and its **parameter object**.

Example:

```json
For Absorption Loss Model
{
  "parameters": {
    "absorption": {...}
  }
}

For ECM 
{
  "parameters": {
    "ecm": {...}
  }
}

For Radar Model
{
  "parameters": {
    "radar": {...}
  }
}
```

---

###  Output

* If valid:

  * Returns **200 OK** with the exact JSON output from the model microservice (e.g. radar, ECM, or absorption).

  Example:

```json
For Absorption Loss Model
{"id".., "ldash": ... }

For ECM 
{ "id".., "jra": ... }

For Radar Model
{ 
  "id".., 
  "pd": ...,
  "sreceiveRadar": ..,
  "siradar": ..
}
```
* If error:
  * Returns **200 OK** with the error in JSON 


  Example:

```json
For Absorption Loss Model
{ "error": ... }
```

---

### File Roles & Flow

* **`AnalysisManagerServiceApplication.java`**

  * Spring Boot main class.
  * Starts the service on port `8084`.

* **`AnalysisController.java`**

  * Defines `/api/analysis/run` endpoint.
  * Accepts composite input (Absorption, ECM, Radar).
  * Delegates processing to `AnalysisService`.

* **`AnalysisService.java`**

  * Sends REST requests to:

    * Absorption model at `http://localhost:8081/api/absorption/calculate`
    * ECM model at `http://localhost:8082/api/ecm/calculate`
    * Radar model at `http://localhost:8083/api/radar/calculate`
  * Aggregates all outputs into a single result JSON.

* **`AnalysisRequest.java`**

  * DTO representing incoming JSON.
  * Holds three sub-objects: `absorptionParams`, `ecmParams`, and `radarParams`.

* **`AnalysisResult.java`**

  * DTO for combined output.
  * Includes: `absorptionOutput`, `ecmOutput`, `radarOutput`.

---

### Database Storage

**None.** This service does **not use a database**. It only acts as a stateless aggregator and coordinator.

---

### Summary of Connections

```
Frontend <--> AnalysisController
                     |
                     v
     +---------------+---------------+
     |               |               |
AbsorptionService ECMModelService RadarModelService
    (8081)            (8082)           (8083)
```

---


