package com.test.task.auth.service;


import com.test.task.auth.model.AccessToken;

import java.security.PublicKey;
import java.util.Map;

public interface AuthService {

    AccessToken sign(Map<String, Object> data);

    PublicKey getPublicKey();
}
