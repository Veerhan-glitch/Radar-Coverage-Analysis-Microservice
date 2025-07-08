package com.xai.radar.model;

// Class to handle atmospheric refraction calculations
public class AtmosphericModel {
    private final double refractivityGradient;

    public AtmosphericModel(double refractivityGradient) {
        this.refractivityGradient = refractivityGradient; // Refractivity gradient per meter
    }

    // Calculate path loss exponent considering atmospheric refraction
    public double[] calculatePathLossExponent(double[] distances) {
        double[] pathLoss = new double[distances.length];
        for (int i = 0; i < distances.length; i++) {
            // Base path loss exponent is 4, adjusted by refractivity gradient
            pathLoss[i] = 4 + refractivityGradient * distances[i];
            // Clip to ensure realistic values (between 2 and 6)
            pathLoss[i] = Math.max(2, Math.min(6, pathLoss[i]));
        }
        return pathLoss;
    }
}