package com.generator.documentation.infrastructure.exception;

/**
 * Custom exception to handle GitHub API specific errors.
 */
public class GitHubApiException extends RuntimeException {
    public GitHubApiException(String message) {
        super(message);
    }
}