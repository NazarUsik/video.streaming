package com.test.task.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security.props")
public record SecurityProperties(
        String issuer,
        String secret,
        String algorithm,
        long expiresAt) {
}
