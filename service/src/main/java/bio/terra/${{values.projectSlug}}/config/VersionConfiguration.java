package bio.terra.${{values.projectSlug}}.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Read from the git.properties file auto-generated at build time */
@ConfigurationProperties("${{values.projectSlug}}.version")
public record VersionConfiguration(String gitHash, String gitTag, String build, String github) {}
