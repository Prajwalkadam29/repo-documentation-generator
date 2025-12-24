package com.generator.documentation.features.collector.service;

import com.generator.documentation.features.collector.client.GitHubClient;
import com.generator.documentation.features.collector.model.ProjectFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubCollectorService {

    private final GitHubClient githubClient;
    private final FileFilterService filterService;

    public List<ProjectFile> collectFromRepository(String owner, String repo) {
        List<ProjectFile> allFiles = new ArrayList<>();
        fetchRecursively(owner, repo, "", allFiles);
        return allFiles;
    }

    private void fetchRecursively(String owner, String repo, String path, List<ProjectFile> accumulator) {
        log.info("Fetching contents for path: {}/{}", repo, path);

        List<ProjectFile> contents = githubClient.getClient().get()
                .uri("/repos/{owner}/{repo}/contents/{path}", owner, repo, path)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (contents == null) return;

        for (ProjectFile item : contents) {
            if ("dir".equals(item.type())) {
                // Recursive call for directories
                fetchRecursively(owner, repo, item.path(), accumulator);
            } else if ("file".equals(item.type()) && filterService.shouldInclude(item.path())) {
                // Fetch specific file content
                accumulator.add(fetchFileDetail(owner, repo, item.path()));
            }
        }
    }

    private ProjectFile fetchFileDetail(String owner, String repo, String path) {
        ProjectFile file = githubClient.getClient().get()
                .uri("/repos/{owner}/{repo}/contents/{path}", owner, repo, path)
                .retrieve()
                .body(ProjectFile.class);

        if (file != null && file.content() != null) {
            // GitHub encodes file content in Base64; we must decode it for the LLM
            String cleanedContent = file.content().replaceAll("\\s", "");
            String decoded = new String(Base64.getDecoder().decode(cleanedContent));

            return ProjectFile.builder()
                    .path(file.path())
                    .content(decoded)
                    .build();
        }
        return file;
    }
}