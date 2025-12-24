package com.generator.documentation.features.collector.service;

import com.generator.documentation.features.collector.model.ProjectFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectorOrchestrator {

    private final GitHubCollectorService githubService;
    private final LocalCollectorService localService;

    public List<ProjectFile> collect(String githubUrl, String localPath) {
        if (githubUrl != null && !githubUrl.isBlank()) {
            // Logic to extract owner/repo from URL: https://github.com/owner/repo
            String[] parts = githubUrl.split("/");
            if (parts.length < 2) throw new IllegalArgumentException("Invalid GitHub URL");
            String owner = parts[parts.length - 2];
            String repo = parts[parts.length - 1];
            return githubService.collectFromRepository(owner, repo);
        } else if (localPath != null && !localPath.isBlank()) {
            return localService.collectFromDirectory(localPath);
        } else {
            throw new IllegalArgumentException("Either GitHub URL or Local Path must be provided");
        }
    }
}