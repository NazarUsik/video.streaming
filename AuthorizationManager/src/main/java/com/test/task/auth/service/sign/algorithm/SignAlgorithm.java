package com.test.task.auth.service.sign.algorithm;

import java.util.Map;

public interface SignAlgorithm {

    String sign(Map<String, Object> headerClaims, Map<String, Object> payloadClaims);

    String algorithm();
}
