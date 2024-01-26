package com.test.task.auth.service;

import com.test.task.auth.model.AccessToken;
import com.test.task.auth.config.SecurityProperties;
import com.test.task.auth.service.sign.SignAlgorithmManager;
import com.test.task.auth.service.vault.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    public static final String BEARER = "Bearer";

    private final SecurityProperties properties;
    private final SignAlgorithmManager signAlgorithmManager;
    private final VaultService vaultService;

    @Override
    public AccessToken sign(Map<String, Object> data) {
        Map<String, Object> headerClaims = requiredClaims();

        return AccessToken.builder()
                .tokenType(BEARER)
                .accessToken(signAlgorithmManager.sign(headerClaims, data))
                .build();
    }

    private Map<String, Object> requiredClaims() {
        Map<String, Object> requiredClaims = new HashMap<>();
        requiredClaims.put(JWTVars.ISSUER, properties.issuer());

        Instant now = Instant.now();

        requiredClaims.put(JWTVars.ISSUED_AT, now.getEpochSecond());
        requiredClaims.put(JWTVars.NOT_BEFORE, now.getEpochSecond());

        long exp = properties.expiresAt();
        if (exp > 0) {
            Duration of = Duration.of(exp, ChronoUnit.SECONDS);
            Instant expTime = now.plus(of);
            requiredClaims.put(JWTVars.EXPIRES_AT, expTime.getEpochSecond());
        }

        requiredClaims.put(JWTVars.JWT_ID, UUID.randomUUID().toString());
        return requiredClaims;
    }

    @Override
    public PublicKey getPublicKey() {
        return vaultService.getPublicKey();
    }
}
