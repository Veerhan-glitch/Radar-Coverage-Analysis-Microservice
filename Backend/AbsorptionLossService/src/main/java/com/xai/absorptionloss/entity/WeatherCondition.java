package com.xai.absorptionloss.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class WeatherCondition {

    // Primary key with auto-increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String weatherType;   // e.g., "Rain", "Fog"
    private boolean wholeGlobe;   // true if weather affects entire globe

    // Geographical bounding box coordinates (lat/long corners)
    private Double lat1;
    private Double lat2;
    private Double lat3;
    private Double lat4;
    private Double long1;
    private Double long2;
    private Double long3;
    private Double long4;

    private Double height;  // Altitude in meters (or km)

    // Many weather conditions can belong to one AtmosphericParameters
    @ManyToOne
    @JoinColumn(name = "parameters_id")
    @JsonBackReference  // Prevents recursive serialization
    private AtmosphericParameters parameters;

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getWeatherType() {
        return weatherType;
    }
    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public boolean isWholeGlobe() {
        return wholeGlobe;
    }
    public void setWholeGlobe(boolean wholeGlobe) {
        this.wholeGlobe = wholeGlobe;
    }

    public Double getlat1() {
        return lat1;
    }
    public void setlat1(Double lat1) {
        this.lat1 = lat1;
    }

    public Double getlat2() {
        return lat2;
    }
    public void setlat2(Double lat2) {
        this.lat2 = lat2;
    }

    public Double getlat3() {
        return lat3;
    }
    public void setlat3(Double lat3) {
        this.lat3 = lat3;
    }

    public Double getlat4() {
        return lat4;
    }
    public void setlat4(Double lat4) {
        this.lat4 = lat4;
    }

    public Double getlong1() {
        return long1;
    }
    public void setlong1(Double long1) {
        this.long1 = long1;
    }

    public Double getlong2() {
        return long2;
    }
    public void setlong2(Double long2) {
        this.long2 = long2;
    }

    public Double getlong3() {
        return long3;
    }
    public void setlong3(Double long3) {
        this.long3 = long3;
    }

    public Double getlong4() {
        return long4;
    }
    public void setlong4(Double long4) {
        this.long4 = long4;
    }

    public Double getheight() {
        return height;
    }
    public void setheight(Double height) {
        this.height = height;
    }

    public AtmosphericParameters getParameters() {
        return parameters;
    }
    public void setParameters(AtmosphericParameters parameters) {
        this.parameters = parameters;
    }
}
