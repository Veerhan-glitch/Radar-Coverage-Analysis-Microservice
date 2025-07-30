package com.xai.ecmmodel;

import com.xai.ecmmodel.entity.ECMOutputs;
import com.xai.ecmmodel.entity.ECMParameters;
import com.xai.ecmmodel.repository.ECMOutputsRepository;
import com.xai.ecmmodel.repository.ECMParametersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Marks this class as a Spring service component
public class EcmService {

    private static final double C = 3e8; // Speed of light in meters/second

    @Autowired
    private ECMParametersRepository inputRepo; // Repository to save input parameters

    @Autowired
    private ECMOutputsRepository outputRepo; // Repository to save calculation output

    // Main method to perform ECM calculations
    public ECMOutputs calculateEcm(ECMParameters input) {
        // Save the input parameters to the database
        inputRepo.save(input);

        // Calculate 3D distance between jammer and radar
        double distance = calculateDistance3D(
            input.getLatJ(), input.getLongJ(), input.getHeightJ(),
            input.getLatR(), input.getLongR(), input.getHeightR()
        );

        // Calculate ERP in dB: Effective Radiated Power
        double erp_dB = 10 * Math.log10(input.getpJ()) 
                        + input.getgJ() 
                        - input.getlJ();

        // Calculate Free Space Path Loss in dB
        double fspl_dB = 20 * Math.log10(
            4 * Math.PI * distance * input.getFreqJ() * 1e9 / C
        );

        // Calculate bandwidth factor and convert to dB
        double bandwidthFactor = Math.min(input.getbJ(), input.getbR()) 
                                 / input.getbR();
        double bandwidth_dB = 10 * Math.log10(bandwidthFactor);

        // Calculate Jamming-to-Radar Advantage in dB
        double j_ra_dB = erp_dB - fspl_dB + bandwidth_dB;

        // Create output object and save result to the database
        ECMOutputs output = new ECMOutputs();
        output.setJRa(j_ra_dB);
        output.setParameters(input);
        return outputRepo.save(output);
    }

    // Helper method to calculate great-circle (2D) distance using the Haversine formula
    private double haversine(double lat1, double lon1,
                             double lat2, double lon2) {
        double R = 6_371_000; // Earth radius in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                 + Math.cos(Math.toRadians(lat1))
                   * Math.cos(Math.toRadians(lat2))
                   * Math.sin(dLon/2)
                   * Math.sin(dLon/2);

        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    // Helper method to calculate full 3D distance including altitude
    private double calculateDistance3D(double lat1, double lon1, double h1,
                                       double lat2, double lon2, double h2) {
        double horizontal = haversine(lat1, lon1, lat2, lon2); // 2D distance
        return Math.sqrt(horizontal * horizontal + Math.pow(h1 - h2, 2)); // Pythagorean distance
    }
}
