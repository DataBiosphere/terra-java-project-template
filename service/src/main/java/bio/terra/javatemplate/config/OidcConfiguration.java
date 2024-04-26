package bio.terra.javatemplate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("oidc")
public record OidcConfiguration(String clientId, String authorityEndpoint) {}
