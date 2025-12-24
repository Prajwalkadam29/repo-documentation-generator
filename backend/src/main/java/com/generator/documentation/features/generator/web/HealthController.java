package com.generator.documentation.features.generator.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health-check")
    public String healthCheck(){
        return "<h1>Project Repository Generator Backend Service is Active!!!</h1>";
    }
}
