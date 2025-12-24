package com.generator.documentation.features.collector.service;

import com.generator.documentation.features.collector.client.GitHubClient;
import com.generator.documentation.features.collector.model.ProjectFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubCollectorServiceTest {

    @Mock private GitHubClient githubClient;
    @Mock private FileFilterService filterService;
    @InjectMocks private GitHubCollectorService collectorService;

    @Test
    void shouldReturnEmptyListIfNoFilesFound() {
        // 1. Mock the deep fluent chain of RestClient
        RestClient restClient = mock(RestClient.class);
        RestClient.RequestHeadersUriSpec getSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // 2. Define the behavior of the chain: .getClient().get().uri().retrieve().body()
        when(githubClient.getClient()).thenReturn(restClient);
        when(restClient.get()).thenReturn(getSpec);
        when(getSpec.uri(anyString(), anyString(), anyString(), anyString())).thenReturn(getSpec);
        when(getSpec.retrieve()).thenReturn(responseSpec);
        // Simulate returning an empty list from the GitHub API
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(List.of());

        // 3. Execute
        List<ProjectFile> result = collectorService.collectFromRepository("owner", "repo");

        // 4. Verify
        assertThat(result).isEmpty();
    }
}