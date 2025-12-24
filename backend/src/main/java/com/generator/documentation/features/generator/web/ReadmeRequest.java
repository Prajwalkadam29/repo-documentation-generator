package com.generator.documentation.features.generator.web;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

public record ReadmeRequest(
        String githubUrl,
        String localPath,
        @NotBlank(message = "AI Service must be specified (gemini, groq, or ollama)")
        String aiService
) {
    @AssertTrue(message = "Either GitHub URL or Local Path must be provided")
    public boolean isValid() {
        return (githubUrl != null && !githubUrl.isBlank()) ||
                (localPath != null && !localPath.isBlank());
    }
}