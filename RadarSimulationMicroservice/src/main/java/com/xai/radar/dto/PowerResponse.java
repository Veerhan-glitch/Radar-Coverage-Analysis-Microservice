package com.xai.radar.dto;

// Data Transfer Object for received power vs. distance response
public class PowerResponse {
    private double[] distances; // Distances in meters
    private double[] receivedPower; // Received power in watts
    private double[] receivedPowerDb; // Received power in dB
    private double minDetectableSignalDb; // Minimum detectable signal in dB

    // Constructor
    public PowerResponse(double[] distances, double[] receivedPower, double[] receivedPowerDb, double minDetectableSignalDb) {
        this.distances = distances;
        this.receivedPower = receivedPower;
        this.receivedPowerDb = receivedPowerDb;
        this.minDetectableSignalDb = minDetectableSignalDb;
    }

    // Getters and setters
    public double[] getDistances() {
        return distances;
    }

    public void setDistances(double[] distances) {
        this.distances = distances;
    }

    public double[] getReceivedPower() {
        return receivedPower;
    }

    public void setReceivedPower(double[] receivedPower) {
        this.receivedPower = receivedPower;
    }

    public double[] getReceivedPowerDb() {
        return receivedPowerDb;
    }

    public void setReceivedPowerDb(double[] receivedPowerDb) {
        this.receivedPowerDb = receivedPowerDb;
    }

    public double getMinDetectableSignalDb() {
        return minDetectableSignalDb;
    }

    public void setMinDetectableSignalDb(double minDetectableSignalDb) {
        this.minDetectableSignalDb = minDetectableSignalDb;
    }
}