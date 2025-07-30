package com.xai.analysismanager.dto;

import java.util.Map;

/**
 * DTO (Data Transfer Object) for receiving analysis request data.
 * Contains scenario ID and a map of input parameters to pass to services.
 */
public class AnalysisRequest {

    // Unique identifier for the scenario being analyzed
    private String scenarioId;

    // Key-value pairs of parameters relevant to the services
    private Map<String, Object> parameters;

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

}
