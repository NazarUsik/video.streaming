package com.test.task.auth.service.vault;


import java.security.PublicKey;
import java.util.Map;

public interface VaultService {
    PublicKey getPublicKey();

    String sign(Map<String, Object> headerClaims, Map<String, Object> payloadClaims);
}
