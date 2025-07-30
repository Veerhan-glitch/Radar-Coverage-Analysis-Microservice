package com.xai.radarmodel.entity;

import jakarta.persistence.*;

@Entity // Marks this class as a JPA entity (represents a DB table)
public class RadarOutputs {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generated ID
    private Long id;

    // Received signal power at the radar (in dB)
    private double sReceiveRadar;

    // Signal-to-interference ratio at radar (in dB)
    private double sIRadar;

    // Probability of detection
    private double pd;

    // Link to input parameters used for the calculation
    @OneToOne
    @JoinColumn(name = "parameter_id") // Foreign key to RadarParameters table
    private RadarParameters parameters;

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getSReceiveRadar() { return sReceiveRadar; }
    public void setSReceiveRadar(double sReceiveRadar) { this.sReceiveRadar = sReceiveRadar; }

    public double getSIRadar() { return sIRadar; }
    public void setSIRadar(double sIRadar) { this.sIRadar = sIRadar; }

    public double getPd() { return pd; }
    public void setPd(double pd) { this.pd = pd; }

    public RadarParameters getParameters() { return parameters; }
    public void setParameters(RadarParameters parameters) { this.parameters = parameters; }
}
