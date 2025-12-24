package com.generator.documentation.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Production-grade configuration mapping.
 * Uses Java Records for immutability and JSR-303 for validation.
 */
@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        @NotBlank String outputDir,
        GithubProperties github
) {
    public record GithubProperties(
            @NotBlank String token,
            @NotEmpty List<String> includePatterns,
            List<String> excludePatterns
    ) {}
}