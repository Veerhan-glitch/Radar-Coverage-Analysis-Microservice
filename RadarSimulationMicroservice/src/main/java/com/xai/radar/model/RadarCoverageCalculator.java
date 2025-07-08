package com.xai.radar.model;

import Jama.Matrix;

// Class to compute radar coverage and received power
public class RadarCoverageCalculator {
    private final RadarSystem radar;
    private final AtmosphericModel atmosphere;
    private final double[] distances;
    private final double pi = Math.PI;

    public RadarCoverageCalculator(RadarSystem radar, AtmosphericModel atmosphere, double[] distances) {
        this.radar = radar;
        this.atmosphere = atmosphere;
        this.distances = distances;
    }

    // Calculate received power using the adjusted radar equation
public double[] calculateReceivedPower() {
    double[] pathLossExponent = atmosphere.calculatePathLossExponent(distances);
    double[] power = new double[distances.length];

    System.out.println("Radar Equation Inputs:");
    System.out.println("  Transmit Power = " + radar.getTransmitPower());
    System.out.println("  Tx Gain        = " + radar.getTransmitGain());
    System.out.println("  Rx Gain        = " + radar.getReceiveGain());
    System.out.println("  Wavelength     = " + radar.getWavelength());
    System.out.println("  RCS            = " + radar.getRcs());

    for (int i = 0; i < distances.length; i++) {
        power[i] = (radar.getTransmitPower() * radar.getTransmitGain() * radar.getReceiveGain() *
                Math.pow(radar.getWavelength(), 2) * radar.getRcs()) /
                (Math.pow(4 * pi, 3) * Math.pow(distances[i], pathLossExponent[i]));

        if (i % 200 == 0) {
            System.out.printf("  d=%.2f  pathLoss=%.2f  power=%.4e%n", distances[i], pathLossExponent[i], power[i]);
        }
    }

    return power;
}

    // Calculate maximum detection range where received power >= minimum detectable signal
    public double getMaxRange(double[] receivedPower) {
        for (int i = receivedPower.length - 1; i >= 0; i--) {
            if (receivedPower[i] >= radar.getMinDetectableSignal()) {
                return distances[i];
            }
        }
        return 0;
    }

    // Generate 3D coverage coordinates (x, y, z) for the coverage volume
    public Matrix[] generateCoverageCoordinates(double maxRange) {
        int azimuthSteps = 360;
        int elevationSteps = 90;
        Matrix x = new Matrix(elevationSteps, azimuthSteps);
        Matrix y = new Matrix(elevationSteps, azimuthSteps);
        Matrix z = new Matrix(elevationSteps, azimuthSteps);

        // Create arrays for azimuth (0 to 2π) and elevation (0 to π/2)
        double[] azimuth = linspace(0, 2 * pi, azimuthSteps);
        double[] elevation = linspace(0, pi / 2, elevationSteps);

        // Convert spherical to Cartesian coordinates
        for (int i = 0; i < elevationSteps; i++) {
            for (int j = 0; j < azimuthSteps; j++) {
                x.set(i, j, maxRange * Math.sin(elevation[i]) * Math.cos(azimuth[j]));
                y.set(i, j, maxRange * Math.sin(elevation[i]) * Math.sin(azimuth[j]));
                z.set(i, j, maxRange * Math.cos(elevation[i]));
            }
        }

        return new Matrix[]{x, y, z};
    }

    // Helper method to create linearly spaced array (similar to np.linspace)
    private double[] linspace(double start, double end, int num) {
        double[] result = new double[num];
        double step = (end - start) / (num - 1);
        for (int i = 0; i < num; i++) {
            result[i] = start + i * step;
        }
        return result;
    }
}