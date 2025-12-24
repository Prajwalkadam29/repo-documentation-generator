package com.generator.documentation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("README Generator Pro API")
                        .version("1.0")
                        .description("API for generating production-grade README.md files using AI (Gemini, Groq, Ollama).")
                        .contact(new Contact()
                                .name("AI Documentation Team")
                                .email("support@generator.com")));
    }
}