package com.generator.documentation.features.collector.client;

import com.generator.documentation.config.AppProperties;
import com.generator.documentation.infrastructure.exception.GitHubApiException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GitHubClient {

    private final RestClient restClient;

    public GitHubClient(RestClient.Builder builder, AppProperties props) {
        this.restClient = builder
                .baseUrl("https://api.github.com")
                .defaultHeader("Authorization", "Bearer " + props.github().token())
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")

                // Production-grade error handling: No more generic 500 errors!
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new GitHubApiException("GitHub Client Error: " + response.getStatusCode() + ". Check your token or URL.");
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new GitHubApiException("GitHub Servers are experiencing issues. Please try again later.");
                })
                .build();
    }

    /**
     * Provides the configured RestClient to other services.
     */
    public RestClient getClient() {
        return this.restClient;
    }
}