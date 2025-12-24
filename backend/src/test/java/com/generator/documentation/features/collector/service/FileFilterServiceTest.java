package com.generator.documentation.features.collector.service;

import com.generator.documentation.config.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class FileFilterServiceTest {

    private FileFilterService filterService;

    @BeforeEach
    void setUp() {
        // Simulating the application.properties configuration
        AppProperties props = new AppProperties("output",
                new AppProperties.GithubProperties("token",
                        List.of("*.java", "**/*.java", "Dockerfile"),
                        List.of("**/target/**", "node_modules/**")
                )
        );
        filterService = new FileFilterService(props);
    }

    @Test
    void shouldIncludeJavaFiles() {
        assertThat(filterService.shouldInclude("src/Main.java")).isTrue();
        assertThat(filterService.shouldInclude("App.java")).isTrue();
    }

    @Test
    void shouldExcludeTargetDirectories() {
        assertThat(filterService.shouldInclude("target/classes/Main.class")).isFalse();
    }

    @Test
    void shouldIncludeDockerfiles() {
        assertThat(filterService.shouldInclude("Dockerfile")).isTrue();
    }
}