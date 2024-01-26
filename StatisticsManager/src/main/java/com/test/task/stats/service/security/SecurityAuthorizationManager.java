package com.test.task.stats.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.task.stats.service.security.auth.AuthorizationAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class SecurityAuthorizationManager {
    private final Map<String, AuthorizationAlgorithm> authorizationAlgorithmMap;
    private final ObjectMapper mapper;

    public SecurityAuthorizationManager(
            List<AuthorizationAlgorithm> algorithms,
            ObjectMapper mapper) {
        this.mapper = mapper;
        this.authorizationAlgorithmMap = algorithms.stream()
                .collect(toMap(AuthorizationAlgorithm::algorithm, identity()));
    }

    public Optional<Authentication> auth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token == null || token.isEmpty()) {
            log.warn("Authorization Header is empty!");
            return Optional.empty();
        }

        String algorithm = getAlgorithm(token);

        AuthorizationAlgorithm authorizationAlgorithm = this.authorizationAlgorithmMap.get(getAlgorithm(token));

        if (authorizationAlgorithm == null) {
            log.warn("{} algorithm is not supported.", algorithm);
            return Optional.empty();
        }

        return authorizationAlgorithm.auth(token);
    }

    @SneakyThrows
    private String getAlgorithm(String token) {
        String jwtHeaderStr = token.split("\\.")[0];
        Map<String, String> jwtHeaders = mapper.readValue(Base64.decodeBase64(jwtHeaderStr), Map.class);

        return jwtHeaders.get("alg");
    }

}
