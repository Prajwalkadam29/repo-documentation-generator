package com.generator.documentation.features.generator.web;

import com.generator.documentation.features.collector.model.ProjectFile;
import com.generator.documentation.features.collector.service.CollectorOrchestrator;
import com.generator.documentation.features.generator.service.AiService;
import com.generator.documentation.features.generator.service.AiServiceFactory;
import com.generator.documentation.infrastructure.web.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documentation")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@io.swagger.v3.oas.annotations.tags.Tag(name = "README Generation", description = "Endpoints for processing codebases and generating documentation")
public class DocumentationController {

    private final CollectorOrchestrator collectorOrchestrator;
    private final AiServiceFactory aiServiceFactory;

    @PostMapping("/generate")
    @io.swagger.v3.oas.annotations.Operation(summary = "Generate a README.md", description = "Analyzes a GitHub URL or Local Path and uses the specified AI service to generate a README.")
    public ApiResponse<String> generateReadme(@Valid @RequestBody ReadmeRequest request) {
        // 1. Collect the files based on the source (GitHub or Local)
        List<ProjectFile> files = collectorOrchestrator.collect(request.githubUrl(), request.localPath());

        if (files.isEmpty()) {
            return ApiResponse.error("No relevant files found in the specified source.");
        }

        // 2. Resolve the AI service dynamically via the factory
        AiService aiService = aiServiceFactory.getService(request.aiService());

        // 3. Generate content using the selected LLM
        String readmeContent = aiService.generate(files);

        return ApiResponse.success(readmeContent, "README generated successfully using " + request.aiService());
    }
}