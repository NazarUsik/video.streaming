package com.test.task.auth.service;

public interface JWTVars {
    String TYPE_JWT = "JWT";

    String ALGORITHM = "alg";
    String TYPE = "typ";

    String JWT_ID = "jti";
    String ISSUER = "iss";
    String EXPIRES_AT = "exp";
    String NOT_BEFORE = "nbf";
    String ISSUED_AT = "iat";
}
