package com.xai.radarmodel;

import com.xai.radarmodel.entity.RadarParameters;
import com.xai.radarmodel.entity.RadarOutputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/radar")
@CrossOrigin(origins = "*")
public class RadarController {

    @Autowired
    private RadarService service;

    @PostMapping("/calculate")
    public RadarOutputs calculate(@RequestBody RadarParameters input) {
        return service.calculateRadar(input);
    }
}
