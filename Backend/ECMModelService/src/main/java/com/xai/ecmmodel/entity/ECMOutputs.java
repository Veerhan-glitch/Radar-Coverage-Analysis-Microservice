package com.xai.ecmmodel.entity;

import jakarta.persistence.*;

@Entity // Marks this class as a JPA entity (mapped to a table)
public class ECMOutputs {

    @Id // Marks the primary key field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID (auto-increment)
    private Long id;

    private double jRa; // Jamming-to-Radar Advantage value

    @OneToOne // One-to-one relationship with ECMParameters
    @JoinColumn(name = "parameter_id") // Foreign key column in the database
    private ECMParameters parameters;

    // Getter and setter


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getJRa() { return jRa; }
    public void setJRa(double jRa) { this.jRa = jRa; }

    public ECMParameters getParameters() { return parameters; }
    public void setParameters(ECMParameters parameters) { this.parameters = parameters; }
}
