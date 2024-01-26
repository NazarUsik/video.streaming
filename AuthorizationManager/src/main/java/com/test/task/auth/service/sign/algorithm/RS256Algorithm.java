package com.test.task.auth.service.sign.algorithm;

import com.test.task.auth.exception.InvalidOperationException;
import com.test.task.auth.config.SecurityProperties;
import com.test.task.auth.service.JWTVars;
import com.test.task.auth.service.vault.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RS256Algorithm implements SignAlgorithm {
    public static final String ALGORITHM = "RS256";
    private final VaultService vaultService;
    private final SecurityProperties properties;

    @Override
    public String sign(Map<String, Object> headerClaims, Map<String, Object> payloadClaims) {
        headerClaims.put(JWTVars.ALGORITHM, ALGORITHM);
        headerClaims.put(JWTVars.TYPE, JWTVars.TYPE_JWT);

        if (properties == null) {
            throw new InvalidOperationException("Security Properties is not found.");
        }

        return vaultService.sign(headerClaims, payloadClaims);
    }

    @Override
    public String algorithm() {
        return ALGORITHM;
    }
}
