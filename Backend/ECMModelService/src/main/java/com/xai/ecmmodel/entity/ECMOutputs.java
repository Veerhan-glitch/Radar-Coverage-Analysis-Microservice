package com.xai.ecmmodel.entity;

import jakarta.persistence.*;

@Entity
public class ECMOutputs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double jRa;

    @OneToOne
    @JoinColumn(name = "parameter_id")
    private ECMParameters parameters;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getJRa() { return jRa; }
    public void setJRa(double jRa) { this.jRa = jRa; }

    public ECMParameters getParameters() { return parameters; }
    public void setParameters(ECMParameters parameters) { this.parameters = parameters; }
}
