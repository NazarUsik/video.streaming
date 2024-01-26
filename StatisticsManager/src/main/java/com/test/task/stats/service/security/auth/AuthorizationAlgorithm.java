package com.test.task.stats.service.security.auth;

import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthorizationAlgorithm {
    Optional<Authentication> auth(String token);

    String algorithm();
}
