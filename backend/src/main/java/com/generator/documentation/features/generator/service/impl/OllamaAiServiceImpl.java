package com.generator.documentation.features.generator.service.impl;

import com.generator.documentation.features.collector.model.ProjectFile;
import com.generator.documentation.features.generator.service.AiService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OllamaAiServiceImpl implements AiService {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/readme-system.st")
    private Resource systemPrompt;

    // Inject the Ollama model
    public OllamaAiServiceImpl(OllamaChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    @Override
    public String generate(List<ProjectFile> files) {
        return chatClient.prompt()
                .system(systemPrompt)
                .user(u -> u.text("Analyze this codebase and generate a README.md:\n\n{codebase}")
                        .param("codebase", formatCodebase(files)))
                .call()
                .content();
    }

    @Override
    public boolean supports(String serviceName) {
        return "ollama".equalsIgnoreCase(serviceName);
    }
}