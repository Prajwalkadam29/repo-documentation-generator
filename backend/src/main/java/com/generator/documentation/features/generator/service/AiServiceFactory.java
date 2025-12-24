package com.generator.documentation.features.generator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AiServiceFactory {

    private final List<AiService> aiServices;

    public AiService getService(String serviceName) {
        return aiServices.stream()
                .filter(service -> service.supports(serviceName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported AI service: " + serviceName));
    }
}