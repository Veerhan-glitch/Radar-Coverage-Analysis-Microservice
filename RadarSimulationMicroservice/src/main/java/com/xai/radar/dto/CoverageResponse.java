package com.xai.radar.dto;

// Data Transfer Object for 3D coverage coordinates
public class CoverageResponse {
    private double[][] x; // X coordinates in meters
    private double[][] y; // Y coordinates in meters
    private double[][] z; // Z coordinates in meters
    private double maxRange; // Maximum detection range in meters

    // Constructor
    public CoverageResponse(double[][] x, double[][] y, double[][] z, double maxRange) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.maxRange = maxRange;
    }

    // Getters and setters
    public double[][] getX() {
        return x;
    }

    public void setX(double[][] x) {
        this.x = x;
    }

    public double[][] getY() {
        return y;
    }

    public void setY(double[][] y) {
        this.y = y;
    }

    public double[][] getZ() {
        return z;
    }

    public void setZ(double[][] z) {
        this.z = z;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }
}