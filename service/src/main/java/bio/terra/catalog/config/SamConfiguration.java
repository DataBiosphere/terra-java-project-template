package bio.terra.catalog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "catalog.sam")
public record SamConfiguration(String basePath) {}
