package com.xai.absorptionloss.entity;

import jakarta.persistence.*;

/**
 * Entity representing the output of the atmospheric absorption loss model.
 * Stores the final result (lDash) and links it to the input parameters.
 */
@Entity
public class AtmosphericOutputs {

    // Primary key for AtmosphericOutputs table (auto-generated)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The computed absorption loss value (L′ or lDash)
    private double lDash;

    // One-to-one relation with the input parameter set
    @OneToOne
    @JoinColumn(name = "parameter_id") // Foreign key to AtmosphericParameters
    private AtmosphericParameters parameters;

    // --- Getters and Setters ---

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public double getLDash() { return lDash; }

    public void setLDash(double lDash) { this.lDash = lDash; }

    public AtmosphericParameters getParameters() { return parameters; }

    public void setParameters(AtmosphericParameters parameters) { this.parameters = parameters; }
}
