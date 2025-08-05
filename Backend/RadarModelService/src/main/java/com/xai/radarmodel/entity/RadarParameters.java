package com.xai.radarmodel.entity;

import jakarta.persistence.*;

@Entity // Marks this class as a JPA entity (maps to a database table)
public class RadarParameters {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    // Radar antenna gain (in dB)
    private double gr;

    // System loss (in dB)
    private double lo;

    // Radar frequency (in GHz)
    private double freqR;

    // Transmit power (in watts)
    private double pr;

    // Bandwidth (in MHz)
    private double br;

    // Receiver noise figure (in dB)
    private double fr;

    // Boltzmann constant
    private double k;

    // System temperature (in Kelvin)
    private double t;

    // Target coordinates (latitude, longitude, and height in meters)
    private double latT, longT, heightT;
    private double latR, longR, heightR;

    // Probability of false alarm (used in detection threshold calculation)
    private double pfa;

    // Getters and Setters for all fields

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getGr() { return gr; }
    public void setGr(double gr) { this.gr = gr; }

    public double getLo() { return lo; }
    public void setLo(double lo) { this.lo = lo; }

    public double getFreqR() { return freqR; }
    public void setFreqR(double freqR) { this.freqR = freqR; }

    public double getPr() { return pr; }
    public void setPr(double pr) { this.pr = pr; }

    public double getBr() { return br; }
    public void setBr(double br) { this.br = br; }

    public double getFr() { return fr; }
    public void setFr(double fr) { this.fr = fr; }

    public double getK() { return k; }
    public void setK(double k) { this.k = k; }

    public double getT() { return t; }
    public void setT(double t) { this.t = t; }

    public double getLatT() { return latT; }
    public void setLatT(double latT) { this.latT = latT; }

    public double getLongT() { return longT; }
    public void setLongT(double longT) { this.longT = longT; }

    public double getHeightT() { return heightT; }
    public void setHeightT(double heightT) { this.heightT = heightT; }

    public double getLatR() { return latR; }
    public void setLatR(double latR) { this.latR = latR; }

    public double getLongR() { return longR; }
    public void setLongR(double longR) { this.longR = longR; }

    public double getHeightR() { return heightR; }
    public void setHeightR(double heightR) { this.heightR = heightR; }

    public double getPfa() { return pfa; }
    public void setPfa(double pfa) { this.pfa = pfa; }
}
