package com.generator.documentation.features.generator.service;

import com.generator.documentation.features.collector.model.ProjectFile;
import java.util.List;
import java.util.stream.Collectors;

public interface AiService {
    String generate(List<ProjectFile> files);
    boolean supports(String serviceName);

    // Default helper method to format files for LLM context
    default String formatCodebase(List<ProjectFile> files) {
        return files.stream()
                .map(file -> "FILE: " + file.path() + "\nCONTENT:\n" + file.content())
                .collect(Collectors.joining("\n\n---\n\n"));
    }
}