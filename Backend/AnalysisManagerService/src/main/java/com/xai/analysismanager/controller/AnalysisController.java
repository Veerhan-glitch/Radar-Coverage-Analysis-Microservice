package com.xai.analysismanager.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import com.xai.analysismanager.dto.AnalysisRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "http://localhost:4200") // Allow frontend to call this API
public class AnalysisController {

    // Injected from application.properties — endpoint URLs of microservices
    @Value("${model.radar.url}")
    private String radarUrl;

    @Value("${model.ecm.url}")
    private String ecmUrl;

    @Value("${model.absorption.url}")
    private String absorptionUrl;

    // WebClient used to forward the request to the actual model microservices
    private final WebClient webClient = WebClient.create();

    // Accepts a POST request with a scenario ID and model input
    @PostMapping("/request")
    public ResponseEntity<Object> proxyModelRequest(@RequestBody AnalysisRequest request) {
        // Ensure only 1 model (radar/ecm/absorption) is requested at a time
        if (request.getParameters().size() != 1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only one model should be requested at a time."));
        }

        // Extract model name and input parameters
        String model = request.getParameters().keySet().iterator().next();
        Object params = request.getParameters().get(model);

        // Get endpoint URL for the requested model
        String endpoint = resolveModelEndpoint(model);
        if (endpoint == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing or unknown model: " + model));
        }

        try {
            // Forward request to the microservice using WebClient
            Object response = webClient.post()
                    .uri(endpoint)
                    .bodyValue(params)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block(); // Blocking call for simplicity

            if (response == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "invalid input"));
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle all possible call failures
            return ResponseEntity.badRequest().body(Map.of("error", "model not running properly"));
        }
    }

    // Maps model name to the corresponding service endpoint
    private String resolveModelEndpoint(String modelName) {
        return switch (modelName.toLowerCase()) {
            case "radar" -> radarUrl;
            case "ecm" -> ecmUrl;
            case "absorption" -> absorptionUrl;
            default -> null;
        };
    }
}
