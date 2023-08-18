package bio.terra.${{values.projectSlug}}.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "${{values.projectSlug}}.sam")
public record SamConfiguration(String basePath) {}
