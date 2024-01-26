package com.test.task.videostream.service.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.task.videostream.client.AuthorizationManagerClient;
import com.test.task.videostream.model.security.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RS256Authorization implements AuthorizationAlgorithm {
    public static final String ALGORITHM = "RS256";

    private final AuthorizationManagerClient authMgrClient;
    private final ObjectMapper mapper;

    @Override
    public Optional<Authentication> auth(String token) {
        String publicKeyStr = authMgrClient.publicKey();

        if (publicKeyStr == null || publicKeyStr.isEmpty()) {
            log.warn("Public key is empty!");
            return Optional.empty();
        }

        User user = parseToken(token, publicKeyStr);
        UsernamePasswordAuthenticationToken authenticated
                = new UsernamePasswordAuthenticationToken(user, null, null);

        return Optional.of(authenticated);
    }

    private User parseToken(String token, String publicKeyStr) {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(publicKey(publicKeyStr))
                .json(new JacksonDeserializer<>())
                .build()
                .parseSignedClaims(token);
        return mapper.convertValue(claims.getPayload().get("user"), User.class);
    }

    @SneakyThrows
    private PublicKey publicKey(String publicKeyStr) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    @Override
    public String algorithm() {
        return ALGORITHM;
    }
}
