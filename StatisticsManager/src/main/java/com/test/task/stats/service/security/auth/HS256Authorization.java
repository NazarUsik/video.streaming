package com.test.task.stats.service.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.task.stats.config.SecurityProperties;
import com.test.task.stats.model.security.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HS256Authorization implements AuthorizationAlgorithm {
    public static final String ALGORITHM = "HS256";

    private final SecurityProperties properties;
    private final ObjectMapper mapper;

    @Override
    public Optional<Authentication> auth(String token) {
        String secret = properties.secret();

        if (secret == null || secret.isEmpty()) {
            log.warn("Secret key is empty!");
            return Optional.empty();
        }

        User user = parseToken(token, secret);
        UsernamePasswordAuthenticationToken authenticated
                = new UsernamePasswordAuthenticationToken(user, null, null);

        return Optional.of(authenticated);
    }

    private User parseToken(String token, String secret) {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .json(new JacksonDeserializer(Maps.of("user", User.class).build()))
                .build()
                .parseSignedClaims(token);
        return mapper.convertValue(claims.getPayload().get("user"), User.class);
    }

    @Override
    public String algorithm() {
        return ALGORITHM;
    }
}
