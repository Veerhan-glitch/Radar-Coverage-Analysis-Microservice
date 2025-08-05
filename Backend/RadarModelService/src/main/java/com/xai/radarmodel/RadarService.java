package com.xai.radarmodel;

import com.xai.radarmodel.entity.RadarParameters;
import com.xai.radarmodel.entity.RadarOutputs;
import com.xai.radarmodel.repository.RadarParametersRepository;
import com.xai.radarmodel.repository.RadarOutputsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Marks this class as a Spring service
public class RadarService {

    @Autowired
    private RadarParametersRepository inputRepo; // Repository to save radar inputs

    @Autowired
    private RadarOutputsRepository outputRepo; // Repository to save radar outputs

    private static final double C = 3e8;       // Speed of light (m/s)
    private static final double SIGMA = 1.0;   // Radar cross-section (constant for now)
    private static final double EPSILON = 0; // Small value to avoid division by zero

    // Main method to perform radar signal and detection calculations
    public RadarOutputs calculateRadar(RadarParameters input) {
        inputRepo.save(input); // Save input to database

        // Calculate wavelength
        double lambda = C / (input.getFreqR() * 1e9);

        // Calculate distance between origin and target radar
        double r = calculateDistance3D(input.getLatR(), input.getLongR(), input.getHeightR(), input.getLatT(), input.getLongT(), input.getHeightT());

        // Convert gain and loss from dB to linear
        double gR = Math.pow(10, input.getGr() / 10);
        double lO = Math.pow(10, input.getLo() / 10);

        // Calculate received power (radar equation)
        double pReceived = (input.getPr() * gR * gR * SIGMA * lambda * lambda) /
                           (Math.pow(4 * Math.PI, 3) * Math.pow(r, 4) * (lO + EPSILON));

        // Convert received power to dB
        double sReceiveRadarDb = 10 * Math.log10(pReceived + EPSILON);

        // Calculate system noise (thermal noise)
        double fRLinear = Math.pow(10, input.getFr() / 10);
        double noise = (input.getK() + EPSILON) * (input.getT() + EPSILON)
                * (input.getBr() * 1e6 + EPSILON) * (fRLinear + EPSILON);
        double noiseDb = 10 * Math.log10(noise + EPSILON);

        // Calculate Signal-to-Interference ratio (SIR) in linear and dB
        double sinrLinear = (pReceived + EPSILON) / (noise + EPSILON);
        double sIRadarDb = 10 * Math.log10(sinrLinear + EPSILON);

        // Calculate SNR and probability of detection (Pd)
        double snrLinear = Math.pow(10, (sReceiveRadarDb - noiseDb) / 10);
        double threshold = input.getPfa() > 0 && input.getPfa() < 1
                         ? Math.sqrt(-Math.log(input.getPfa()))
                         : 0;
        double pd = 0.5 * (1 + erf((Math.sqrt(snrLinear) - threshold) / Math.sqrt(2)));

        // Create output object and save to database
        RadarOutputs output = new RadarOutputs();
        output.setSReceiveRadar(sReceiveRadarDb);
        output.setSIRadar(sIRadarDb);
        output.setPd(Double.isNaN(pd) ? 0.0 : pd); // Handle NaN case
        output.setParameters(input);

        return outputRepo.save(output); // Save and return result
    }

    // Calculate distance over Earth's surface using haversine formula
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371e3; // Earth radius in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    // Calculate full 3D distance (includes altitude difference)
    private double calculateDistance3D(double lat1, double lon1, double h1,
                                       double lat2, double lon2, double h2) {
        double d = haversine(lat1, lon1, lat2, lon2);
        return Math.sqrt(d * d + Math.pow(h1 - h2, 2));
    }

    // Approximate error function (used for Pd calculation)
    private double erf(double x) {
        double t = 1.0 / (1.0 + 0.5 * Math.abs(x));
        double tau = t * Math.exp(-x * x - 1.26551223 + t * (1.00002368 +
                    t * (0.37409196 + t * (0.09678418 + t * (-0.18628806 +
                    t * (0.27886807 + t * (-1.13520398 + t * (1.48851587 +
                    t * (-0.82215223 + t * 0.17087277)))))))));
        return x >= 0 ? 1 - tau : tau - 1;
    }
}
