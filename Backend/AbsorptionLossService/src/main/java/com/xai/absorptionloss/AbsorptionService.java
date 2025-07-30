package com.xai.absorptionloss;

import com.xai.absorptionloss.entity.AtmosphericOutputs;
import com.xai.absorptionloss.entity.AtmosphericParameters;
import com.xai.absorptionloss.entity.WeatherCondition;
import com.xai.absorptionloss.repository.AtmosphericOutputsRepository;
import com.xai.absorptionloss.repository.AtmosphericParametersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbsorptionService {

    @Autowired
    private AtmosphericParametersRepository inputRepo;

    @Autowired
    private AtmosphericOutputsRepository outputRepo;

    // Main method that handles the full loss calculation
    public AtmosphericOutputs calculateAbsorption(AtmosphericParameters input) {
        inputRepo.save(input); // Persist input for logging/auditing

        // Extract transmitter location
        double lat2 = input.getLatT();
        double lon2 = input.getLongT();
        double h2 = input.getHeightT();

        // Calculate total distance between receiver and transmitter (3D)
        double distance = calculateDistance3D(
            input.getLatR(), input.getLongR(), input.getHeightR(),
            lat2, lon2, h2
        );

        // Compute atmospheric absorption coefficient (α)
        double alpha = calculateAlpha(input);

        // Total absorption loss (dB/km * km)
        double lDashDb = alpha * (distance / 1000.0);

        // Package and save output
        AtmosphericOutputs output = new AtmosphericOutputs();
        output.setLDash(lDashDb);
        output.setParameters(input);
        return outputRepo.save(output);
    }

    // Calculates absorption coefficient α based on weather conditions and frequency
    private double calculateAlpha(AtmosphericParameters input) {
        List<WeatherCondition> conditions = input.getWeatherConditions();
        if (conditions == null || conditions.isEmpty()) {
            return 0.01; // Default: clear weather (light loss)
        }

        double totalAlpha = 0.0;
        for (WeatherCondition condition : conditions) {
            String type = condition.getWeatherType().toLowerCase();
            switch (type) {
                case "rain":
                    // Empirical formula for rain attenuation
                    totalAlpha += 0.0101 * Math.pow(input.getFreqOp(), 1.276) *
                                  Math.pow(input.getRfr(), 0.9991);
                    break;
                case "snow":
                    // Empirical formula for snow attenuation
                    totalAlpha += 0.0001 * Math.pow(input.getFreqOp(), 2) *
                                  input.getSfr() + 0.02;
                    break;
                case "fog":
                    // Based on visibility and frequency (if visibility > 0)
                    if (input.getVisFog() > 0) {
                        totalAlpha += (0.05 * Math.pow(input.getFreqOp(), 2)) /
                                      (input.getVisFog() / 1000.0); // visibility in km
                    }
                    break;
                case "clear":
                default:
                    totalAlpha += 0.01; // baseline value
                    break;
            }
        }
        return totalAlpha;
    }

    // Calculates the haversine (great-circle) distance between two lat/lon points in meters
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371e3; // Earth's radius in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    // Combines 2D surface distance and altitude difference to compute full 3D distance
    private double calculateDistance3D(double lat1, double lon1, double h1,
                                       double lat2, double lon2, double h2) {
        double horizontal = haversine(lat1, lon1, lat2, lon2); // surface distance in meters
        return Math.sqrt(horizontal * horizontal + Math.pow(h1 - h2, 2)); // Pythagorean 3D
    }
}
