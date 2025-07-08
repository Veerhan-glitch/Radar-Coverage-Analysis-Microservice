package com.xai.radar.dto;

public class RadarRequest {
    private double transmitPower;
    private double transmitGain;
    private double receiveGain;
    private double frequency;
    private double rcs;
    private double minDetectableSignal;
    private double refractivityGradient;

    // Getters and setters
    public double getTransmitPower() { return transmitPower; }
    public void setTransmitPower(double transmitPower) { this.transmitPower = transmitPower; }

    public double getTransmitGain() { return transmitGain; }
    public void setTransmitGain(double transmitGain) { this.transmitGain = transmitGain; }

    public double getReceiveGain() { return receiveGain; }
    public void setReceiveGain(double receiveGain) { this.receiveGain = receiveGain; }

    public double getFrequency() { return frequency; }
    public void setFrequency(double frequency) { this.frequency = frequency; }

    public double getRcs() { return rcs; }
    public void setRcs(double rcs) { this.rcs = rcs; }

    public double getMinDetectableSignal() { return minDetectableSignal; }
    public void setMinDetectableSignal(double minDetectableSignal) { this.minDetectableSignal = minDetectableSignal; }

    public double getRefractivityGradient() { return refractivityGradient; }
    public void setRefractivityGradient(double refractivityGradient) { this.refractivityGradient = refractivityGradient; }
}
