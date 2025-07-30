package com.xai.absorptionloss.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class AtmosphericParameters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    // --- Weather-related coefficients and frequency values ---
    private double rfr;     // Rainfall rate (mm/hr)
    private double sfr;     // Snowfall rate
    private double visFog;  // Fog visibility (meters)
    private double freqR;   // Receiver frequency
    private double freqJ;   // Jammer frequency
    private double freqOp;  // Operating frequency used for absorption calculations

    // --- Receiver location ---
    private double latR;
    private double longR;
    private double heightR;

    // --- Transmitter location ---
    private double latT;
    private double longT;
    private double heightT;

    // --- Jammer location ---
    private double latJ;
    private double longJ;
    private double heightJ;

    // One-to-many relationship: each parameter set can have multiple weather conditions
    @OneToMany(mappedBy = "parameters", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<WeatherCondition> weatherConditions;

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public double getRfr() {
        return rfr;
    }
    public void setRfr(double rfr) {
        this.rfr = rfr;
    }

    public double getSfr() {
        return sfr;
    }
    public void setSfr(double sfr) {
        this.sfr = sfr;
    }

    public double getVisFog() {
        return visFog;
    }
    public void setVisFog(double visFog) {
        this.visFog = visFog;
    }

    public double getFreqR() {
        return freqR;
    }
    public void setFreqR(double freqR) {
        this.freqR = freqR;
    }

    public double getFreqJ() {
        return freqJ;
    }
    public void setFreqJ(double freqJ) {
        this.freqJ = freqJ;
    }

    public double getFreqOp() {
        return freqOp;
    }
    public void setFreqOp(double freqOp) {
        this.freqOp = freqOp;
    }

    public double getLatR() {
        return latR;
    }
    public void setLatR(double latR) {
        this.latR = latR;
    }

    public double getLongR() {
        return longR;
    }
    public void setLongR(double longR) {
        this.longR = longR;
    }

    public double getHeightR() {
        return heightR;
    }
    public void setHeightR(double heightR) {
        this.heightR = heightR;
    }

    public double getLatT() {
        return latT;
    }
    public void setLatT(double latT) {
        this.latT = latT;
    }

    public double getLongT() {
        return longT;
    }
    public void setLongT(double longT) {
        this.longT = longT;
    }

    public double getHeightT() {
        return heightT;
    }
    public void setHeightT(double heightT) {
        this.heightT = heightT;
    }

    public double getLatJ() {
        return latJ;
    }
    public void setLatJ(double latJ) {
        this.latJ = latJ;
    }

    public double getLongJ() {
        return longJ;
    }
    public void setLongJ(double longJ) {
        this.longJ = longJ;
    }

    public double getHeightJ() {
        return heightJ;
    }
    public void setHeightJ(double heightJ) {
        this.heightJ = heightJ;
    }

    public List<WeatherCondition> getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(List<WeatherCondition> weatherConditions) {
        this.weatherConditions = weatherConditions;

        // Ensure bidirectional linkage for JPA cascade persist
        if (weatherConditions != null) {
            for (WeatherCondition wc : weatherConditions) {
                wc.setParameters(this);
            }
        }
    }
}
