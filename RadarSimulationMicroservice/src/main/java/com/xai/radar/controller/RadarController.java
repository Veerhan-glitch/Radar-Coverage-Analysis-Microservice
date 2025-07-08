package com.xai.radar.controller;

import com.xai.radar.dto.CoverageResponse;
import com.xai.radar.dto.PowerResponse;
import com.xai.radar.dto.RadarRequest;
import com.xai.radar.model.AtmosphericModel;
import com.xai.radar.model.RadarCoverageCalculator;
import com.xai.radar.model.RadarSystem;
import Jama.Matrix;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/radar")
@CrossOrigin(origins = "*") // Allows access from any frontend
public class RadarController {

    // === GET Endpoints ===

    // @GetMapping("/power")
    // public PowerResponse getPowerVsDistance() {
    //     RadarSystem radar = new RadarSystem();
    //     AtmosphericModel atmosphere = new AtmosphericModel(-39e-6);
    //     double[] distances = linspace(1e3, 3e6, 1000);
    //     RadarCoverageCalculator calculator = new RadarCoverageCalculator(radar, atmosphere, distances);

    //     double[] receivedPower = calculator.calculateReceivedPower();
    //     double[] receivedPowerDb = new double[receivedPower.length];
    //     for (int i = 0; i < receivedPower.length; i++) {
    //         receivedPowerDb[i] = 10 * Math.log10(receivedPower[i]);
    //     }
    //     double minDetectableSignalDb = 10 * Math.log10(radar.getMinDetectableSignal());

    //     return new PowerResponse(distances, receivedPower, receivedPowerDb, minDetectableSignalDb);
    // }

    // @GetMapping("/coverage")
    // public CoverageResponse getCoverageArea() {
    //     RadarSystem radar = new RadarSystem();
    //     AtmosphericModel atmosphere = new AtmosphericModel(-39e-6);
    //     double[] distances = linspace(1e3, 3e6, 1000);
    //     RadarCoverageCalculator calculator = new RadarCoverageCalculator(radar, atmosphere, distances);

    //     double[] receivedPower = calculator.calculateReceivedPower();
    //     double maxRange = calculator.getMaxRange(receivedPower);
    //     Matrix[] coordinates = calculator.generateCoverageCoordinates(maxRange);

    //     double[][] x = coordinates[0].getArray();
    //     double[][] y = coordinates[1].getArray();
    //     double[][] z = coordinates[2].getArray();

    //     return new CoverageResponse(x, y, z, maxRange);
    // }

    // === POST Endpoints (take input from frontend) ===

    // @PostMapping("/power")
    // public PowerResponse customPower(@RequestBody RadarRequest request) {
    //     RadarSystem radar = new RadarSystem(
    //             request.getTransmitPower(),
    //             request.getTransmitGain(),
    //             request.getReceiveGain(),
    //             request.getFrequency(),
    //             request.getRcs(),
    //             request.getMinDetectableSignal()
    //     );
    //     AtmosphericModel atmosphere = new AtmosphericModel(request.getRefractivityGradient());
    //     double[] distances = linspace(1e3, 3e6, 1000);
    //     RadarCoverageCalculator calculator = new RadarCoverageCalculator(radar, atmosphere, distances);

    //     double[] receivedPower = calculator.calculateReceivedPower();
    //     double[] receivedPowerDb = new double[receivedPower.length];
    //     for (int i = 0; i < receivedPower.length; i++) {
    //         receivedPowerDb[i] = 10 * Math.log10(receivedPower[i]);
    //     }
    //     double minDetectableSignalDb = 10 * Math.log10(radar.getMinDetectableSignal());

    //     return new PowerResponse(distances, receivedPower, receivedPowerDb, minDetectableSignalDb);
    // }


@PostMapping("/power")
public PowerResponse getPowerWithInput(@RequestBody RadarRequest request) {
    RadarSystem radar = new RadarSystem(
        request.getTransmitPower(),
        request.getTransmitGain(),
        request.getReceiveGain(),
        request.getFrequency(),
        request.getRcs(),
        request.getMinDetectableSignal()
    );
    AtmosphericModel atmosphere = new AtmosphericModel(request.getRefractivityGradient());

    double[] distances = linspace(1e3, 3e6, 1000);
    RadarCoverageCalculator calculator = new RadarCoverageCalculator(radar, atmosphere, distances);

    double[] receivedPower = calculator.calculateReceivedPower();
    double[] receivedPowerDb = new double[receivedPower.length];
    for (int i = 0; i < receivedPower.length; i++) {
        receivedPowerDb[i] = 10 * Math.log10(receivedPower[i]);
    }
    double minDetectableSignalDb = 10 * Math.log10(radar.getMinDetectableSignal());

    return new PowerResponse(distances, receivedPower, receivedPowerDb, minDetectableSignalDb);
}


    @PostMapping("/coverage")
    public CoverageResponse customCoverage(@RequestBody RadarRequest request) {
        RadarSystem radar = new RadarSystem(
                request.getTransmitPower(),
                request.getTransmitGain(),
                request.getReceiveGain(),
                request.getFrequency(),
                request.getRcs(),
                request.getMinDetectableSignal()
        );
        AtmosphericModel atmosphere = new AtmosphericModel(request.getRefractivityGradient());
        double[] distances = linspace(1e3, 3e6, 1000);
        RadarCoverageCalculator calculator = new RadarCoverageCalculator(radar, atmosphere, distances);

        double[] receivedPower = calculator.calculateReceivedPower();
        double maxRange = calculator.getMaxRange(receivedPower);
        Matrix[] coordinates = calculator.generateCoverageCoordinates(maxRange);

        double[][] x = coordinates[0].getArray();
        double[][] y = coordinates[1].getArray();
        double[][] z = coordinates[2].getArray();

        return new CoverageResponse(x, y, z, maxRange);
    }

    // Helper method to generate linearly spaced values
    private double[] linspace(double start, double end, int num) {
        double[] result = new double[num];
        double step = (end - start) / (num - 1);
        for (int i = 0; i < num; i++) {
            result[i] = start + i * step;
        }
        return result;
    }
}
