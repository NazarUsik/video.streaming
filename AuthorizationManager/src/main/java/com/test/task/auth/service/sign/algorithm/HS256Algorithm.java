package com.test.task.auth.service.sign.algorithm;

import com.test.task.auth.exception.InvalidOperationException;
import com.test.task.auth.config.SecurityProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HS256Algorithm implements SignAlgorithm {
    public static final String ALGORITHM = "HS256";
    private final SecurityProperties props;

    @SneakyThrows
    @Override
    public String sign(Map<String, Object> headerClaims, Map<String, Object> payloadClaims) {
        String secret = props.secret();

        if (secret == null || secret.isEmpty()) {
            throw new InvalidOperationException("Secret is not found.");
        }

        return Jwts.builder()
                .header().add(headerClaims).and()
                .claims(payloadClaims)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public String algorithm() {
        return ALGORITHM;
    }
}
