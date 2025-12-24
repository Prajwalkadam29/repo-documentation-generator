package com.generator.documentation.features.collector.model;

import lombok.Builder;

/**
 * A standard representation of a code file.
 * We use Lombok @Builder for easy object creation.
 */
@Builder
public record ProjectFile(
        String path,
        String content,
        long size,
        String type // "file" or "dir"
) {}