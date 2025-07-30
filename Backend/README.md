## Backend

### Overview

This folder contains the backend services that power the radar modeling platform. It includes **three computational models**, an **authentication module**, and an **analysis manager** for coordinating inter-service logic.

---

### Structure

* **1. RadarModelService**
  Calculates radar performance metrics like received signal, SIR, and probability of detection.

* **2. ECMModelService**
  Estimates ECM (Electronic Countermeasure) effectiveness such as  jamming strength.

* **3. AbsorptionLossService**
  Computes atmospheric absorption loss based on terrain and weather data.

* **4. AuthService**
  Handles user authentication.

* **5. AnalysisManager**
  Receives input from the frontend and forwards it to the appropriate model service. 

---

Each service is built using Spring Boot and communicates via REST APIs. PostgreSQL is used for persistence across services.

---