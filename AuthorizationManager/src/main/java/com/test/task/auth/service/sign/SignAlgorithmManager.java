package com.test.task.auth.service.sign;

import com.test.task.auth.config.SecurityConfig;
import com.test.task.auth.config.SecurityProperties;
import com.test.task.auth.exception.InvalidOperationException;
import com.test.task.auth.service.sign.algorithm.HS256Algorithm;
import com.test.task.auth.service.sign.algorithm.RS256Algorithm;
import com.test.task.auth.service.sign.algorithm.SignAlgorithm;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class SignAlgorithmManager {
    private final Map<String, SignAlgorithm> signAlgorithmMap;
    private final SecurityProperties properties;

    public SignAlgorithmManager(
            SecurityProperties properties,
            List<SignAlgorithm> algorithms) {
        this.properties = properties;
        this.signAlgorithmMap = algorithms.stream()
                .collect(toMap(SignAlgorithm::algorithm, identity()));
    }

    public String sign(Map<String, Object> headerClaims, Map<String, Object> payloadClaims) {
        String algorithmType = properties.algorithm();
        if (algorithmType == null || algorithmType.isEmpty()) {
            throw new InvalidOperationException("Algorithm type is null or empty.");
        }

        SignAlgorithm signAlgorithm = signAlgorithmMap.get(algorithmType.toUpperCase());

        if (signAlgorithm == null) {
            throw new InvalidOperationException("Algorithm type is invalid.");
        }

        return signAlgorithm.sign(headerClaims, payloadClaims);
    }


}
