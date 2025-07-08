package com.xai.radar.model;

// Class to manage radar system parameters
public class RadarSystem {
    private double transmitPower = 1e6; // Transmit power in watts (1 MW)
    private double transmitGain = 30;   // Transmit antenna gain (linear scale)
    private double receiveGain = 30;    // Receive antenna gain (linear scale)
    private double frequency = 10e6;    // Operating frequency in Hz (10 MHz)
    private double rcs = 1;             // Radar Cross Section in m^2
    private double minDetectableSignal = 1e-13; // Minimum Detectable Signal in watts
    private double speedOfLight = 3e8;  // Speed of light in m/s
    private double wavelength;          // Wavelength in meters

    // Parameterized constructor (used by controller)
    public RadarSystem(double transmitPower, double transmitGain, double receiveGain,
                       double frequency, double rcs, double minDetectableSignal) {
        System.out.println("⚠️ Parameterized RadarSystem constructor called");
        this.transmitPower = transmitPower;
        this.transmitGain = transmitGain;
        this.receiveGain = receiveGain;
        this.frequency = frequency;
        this.rcs = rcs;
        this.minDetectableSignal = minDetectableSignal;
        this.wavelength = speedOfLight / frequency;

        System.out.println("Initialized with:");
        System.out.println("  Transmit Power: " + this.transmitPower);
        System.out.println("  Transmit Gain : " + this.transmitGain);
        System.out.println("  Receive Gain  : " + this.receiveGain);
        System.out.println("  Frequency     : " + this.frequency);
        System.out.println("  RCS           : " + this.rcs);
        System.out.println("  Min Signal    : " + this.minDetectableSignal);
        System.out.println("  Wavelength    : " + this.wavelength);
    }

    // Default constructor (only used by old / hardcoded paths)
    public RadarSystem() {
        System.out.println("⚠️ Default RadarSystem constructor called");
        this.wavelength = speedOfLight / frequency;
    }

    // Getters
    public double getTransmitPower() {
        return transmitPower;
    }

    public double getTransmitGain() {
        return transmitGain;
    }

    public double getReceiveGain() {
        return receiveGain;
    }

    public double getWavelength() {
        return wavelength;
    }

    public double getRcs() {
        return rcs;
    }

    public double getMinDetectableSignal() {
        return minDetectableSignal;
    }
}
