package com.xai.ecmmodel;

import com.xai.ecmmodel.entity.ECMOutputs;
import com.xai.ecmmodel.entity.ECMParameters;
import com.xai.ecmmodel.repository.ECMOutputsRepository;
import com.xai.ecmmodel.repository.ECMParametersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EcmService {

    private static final double C = 3e8;

    @Autowired
    private ECMParametersRepository inputRepo;

    @Autowired
    private ECMOutputsRepository outputRepo;

    public ECMOutputs calculateEcm(ECMParameters input) {
        // 1. Persist the input parameters
        inputRepo.save(input);

        // 2. Compute 3D distance
        double distance = calculateDistance3D(
            input.getLatJ(), input.getLongJ(), input.getHeightJ(),
            input.getLatR(), input.getLongR(), input.getHeightR()
        );

        // 3. Compute ERP_dB: 10*log10(pJ) + gJ - lJ
        double erp_dB = 10 * Math.log10(input.getpJ()) 
                        + input.getgJ() 
                        - input.getlJ();

        // 4. Compute FSPL_dB: 20*log10(4π·distance·freqJ·1e9 / C)
        double fspl_dB = 20 * Math.log10(
            4 * Math.PI * distance * input.getFreqJ() * 1e9 / C
        );

        // 5. Compute bandwidthFactor = min(bJ, bR) / bR, then dB
        double bandwidthFactor = Math.min(input.getbJ(), input.getbR()) 
                                 / input.getbR();
        double bandwidth_dB = 10 * Math.log10(bandwidthFactor);

        // 6. Jamming‑to‑Radar Advantage j_ra_dB
        double j_ra_dB = erp_dB - fspl_dB + bandwidth_dB;

        // 7. Prepare and persist the output
        ECMOutputs output = new ECMOutputs();
        output.setJRa(j_ra_dB);
        output.setParameters(input);
        return outputRepo.save(output);
    }

    private double haversine(double lat1, double lon1,
                             double lat2, double lon2) {
        double R = 6_371_000; // radius in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                 + Math.cos(Math.toRadians(lat1))
                   * Math.cos(Math.toRadians(lat2))
                   * Math.sin(dLon/2)
                   * Math.sin(dLon/2);

        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private double calculateDistance3D(double lat1, double lon1, double h1,
                                       double lat2, double lon2, double h2) {
        double horizontal = haversine(lat1, lon1, lat2, lon2);
        return Math.sqrt(horizontal*horizontal + Math.pow(h1 - h2, 2));
    }
}
