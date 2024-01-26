package com.test.task.stats.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security.props")
public record SecurityProperties(
        String secret
) {
}
