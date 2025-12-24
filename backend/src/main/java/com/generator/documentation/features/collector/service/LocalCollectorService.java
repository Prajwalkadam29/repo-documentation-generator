package com.generator.documentation.features.collector.service;

import com.generator.documentation.features.collector.model.ProjectFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalCollectorService {

    private final FileFilterService filterService;

    /**
     * Walks through a local directory and collects relevant code files.
     * * @param rootPath The absolute path to the local project.
     * @return A list of ProjectFile objects containing the code.
     */
    public List<ProjectFile> collectFromDirectory(String rootPath) {
        Path start = Path.of(rootPath).toAbsolutePath().normalize();

        if (!Files.exists(start) || !Files.isDirectory(start)) {
            log.error("Invalid local directory path: {}", rootPath);
            throw new IllegalArgumentException("Path is not a valid directory: " + rootPath);
        }

        List<ProjectFile> collectedFiles = new ArrayList<>();

        // try-with-resources is vital here to close the file stream
        try (Stream<Path> stream = Files.walk(start)) {
            stream.filter(Files::isRegularFile)
                    .forEach(path -> {
                        // We need the path relative to the root for the LLM to understand structure
                        String relativePath = start.relativize(path).toString().replace('\\', '/');

                        if (filterService.shouldInclude(relativePath)) {
                            try {
                                String content = Files.readString(path);
                                collectedFiles.add(ProjectFile.builder()
                                        .path(relativePath)
                                        .content(content)
                                        .size(Files.size(path))
                                        .build());
                                log.debug("Successfully read local file: {}", relativePath);
                            } catch (IOException e) {
                                log.warn("Could not read file {}, skipping. Error: {}", path, e.getMessage());
                            }
                        }
                    });
        } catch (IOException e) {
            log.error("Error traversing directory: {}", rootPath, e);
            throw new RuntimeException("Failed to process local directory", e);
        }

        return collectedFiles;
    }
}