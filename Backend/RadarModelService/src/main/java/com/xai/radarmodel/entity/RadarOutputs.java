package com.xai.radarmodel.entity;

import jakarta.persistence.*;

@Entity
public class RadarOutputs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double sReceiveRadar;
    private double sIRadar;
    private double pd;

    @OneToOne
    @JoinColumn(name = "parameter_id")
    private RadarParameters parameters;

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