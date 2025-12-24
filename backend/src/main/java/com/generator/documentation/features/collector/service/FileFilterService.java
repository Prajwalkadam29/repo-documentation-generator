package com.generator.documentation.features.collector.service;

import com.generator.documentation.config.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileFilterService {

    private final List<PathMatcher> includeMatchers;
    private final List<PathMatcher> excludeMatchers;

    public FileFilterService(AppProperties props) {
        this.includeMatchers = props.github().includePatterns().stream()
                .map(p -> FileSystems.getDefault().getPathMatcher("glob:" + p.trim()))
                .collect(Collectors.toList());

        this.excludeMatchers = props.github().excludePatterns().stream()
                .map(p -> FileSystems.getDefault().getPathMatcher("glob:" + p.trim()))
                .collect(Collectors.toList());
    }

    public boolean shouldInclude(String filePath) {
        Path path = Path.of(filePath);

        // 1. Check exclusions first (highest priority)
        boolean isExcluded = excludeMatchers.stream().anyMatch(m -> m.matches(path));
        if (isExcluded) {
            log.debug("Excluding file: {}", filePath);
            return false;
        }

        // 2. Check inclusions
        return includeMatchers.stream().anyMatch(m -> m.matches(path));
    }
}