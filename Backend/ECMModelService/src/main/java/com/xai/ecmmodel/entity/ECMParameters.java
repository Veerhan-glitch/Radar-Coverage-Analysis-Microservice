package com.xai.ecmmodel.entity;

import jakarta.persistence.*;

@Entity
public class ECMParameters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double pJ, gJ, lJ;
    private double azimuthJ, elevationJ;
    private double beamwidthAzJ, beamwidthElJ;
    private double bJ, freqJ;
    private double latJ, longJ, heightJ;
    private String jammerType;
    private double latR, longR, heightR, bR, freqR;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getpJ() { return pJ; }
    public void setpJ(double pJ) { this.pJ = pJ; }

    public double getgJ() { return gJ; }
    public void setgJ(double gJ) { this.gJ = gJ; }

    public double getlJ() { return lJ; }
    public void setlJ(double lJ) { this.lJ = lJ; }

    public double getAzimuthJ() { return azimuthJ; }
    public void setAzimuthJ(double azimuthJ) { this.azimuthJ = azimuthJ; }

    public double getElevationJ() { return elevationJ; }
    public void setElevationJ(double elevationJ) { this.elevationJ = elevationJ; }

    public double getBeamwidthAzJ() { return beamwidthAzJ; }
    public void setBeamwidthAzJ(double beamwidthAzJ) { this.beamwidthAzJ = beamwidthAzJ; }

    public double getBeamwidthElJ() { return beamwidthElJ; }
    public void setBeamwidthElJ(double beamwidthElJ) { this.beamwidthElJ = beamwidthElJ; }

    public double getbJ() { return bJ; }
    public void setbJ(double bJ) { this.bJ = bJ; }

    public double getFreqJ() { return freqJ; }
    public void setFreqJ(double freqJ) { this.freqJ = freqJ; }

    public double getLatJ() { return latJ; }
    public void setLatJ(double latJ) { this.latJ = latJ; }

    public double getLongJ() { return longJ; }
    public void setLongJ(double longJ) { this.longJ = longJ; }

    public double getHeightJ() { return heightJ; }
    public void setHeightJ(double heightJ) { this.heightJ = heightJ; }

    public String getJammerType() { return jammerType; }
    public void setJammerType(String jammerType) { this.jammerType = jammerType; }

    public double getLatR() { return latR; }
    public void setLatR(double latR) { this.latR = latR; }

    public double getLongR() { return longR; }
    public void setLongR(double longR) { this.longR = longR; }

    public double getHeightR() { return heightR; }
    public void setHeightR(double heightR) { this.heightR = heightR; }

    public double getbR() { return bR; }
    public void setbR(double bR) { this.bR = bR; }

    public double getFreqR() { return freqR; }
    public void setFreqR(double freqR) { this.freqR = freqR; }
}
