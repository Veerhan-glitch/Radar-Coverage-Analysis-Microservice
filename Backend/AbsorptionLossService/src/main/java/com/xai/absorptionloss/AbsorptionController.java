package com.xai.absorptionloss;

import com.xai.absorptionloss.entity.AtmosphericParameters;
import com.xai.absorptionloss.entity.AtmosphericOutputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/absorption")
@CrossOrigin(origins = "*")
public class AbsorptionController {

    @Autowired
    private AbsorptionService service;

    @PostMapping("/calculate")
    public AtmosphericOutputs calculate(@RequestBody AtmosphericParameters input) {
        return service.calculateAbsorption(input);
    }
}