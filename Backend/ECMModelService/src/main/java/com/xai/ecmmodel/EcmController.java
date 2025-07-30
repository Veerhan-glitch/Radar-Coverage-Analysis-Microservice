package com.xai.ecmmodel;

import com.xai.ecmmodel.entity.ECMParameters;
import com.xai.ecmmodel.entity.ECMOutputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecm")
@CrossOrigin(origins = "http://localhost:4200")
public class EcmController {

    @Autowired
    private EcmService service;

    @PostMapping("/calculate")
    public ECMOutputs calculate(@RequestBody ECMParameters input) {
        return service.calculateEcm(input);
    }
}
