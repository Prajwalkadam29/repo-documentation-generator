package com.generator.documentation.features.generator.service;

import com.generator.documentation.features.generator.service.impl.GeminiAiServiceImpl;
import com.generator.documentation.features.generator.service.impl.OllamaAiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class AiServiceFactoryTest {

    private AiServiceFactory factory;
    private AiService geminiService;
    private AiService ollamaService;

    @BeforeEach
    void setUp() {
        geminiService = Mockito.mock(GeminiAiServiceImpl.class);
        ollamaService = Mockito.mock(OllamaAiServiceImpl.class);

        when(geminiService.supports("gemini")).thenReturn(true);
        when(ollamaService.supports("ollama")).thenReturn(true);

        factory = new AiServiceFactory(List.of(geminiService, ollamaService));
    }

    @Test
    void shouldReturnGeminiService() {
        assertThat(factory.getService("gemini")).isEqualTo(geminiService);
    }

    @Test
    void shouldThrowExceptionForUnsupportedService() {
        assertThatThrownBy(() -> factory.getService("unknown"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}