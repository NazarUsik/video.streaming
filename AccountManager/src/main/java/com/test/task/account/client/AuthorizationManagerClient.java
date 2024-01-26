package com.test.task.account.client;

import com.test.task.account.model.security.AccessToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "authorization.manager", path = "/auth", fallback = AuthorizationManagerClient.Fallback.class)
public interface AuthorizationManagerClient {
    @GetMapping(path = "/discovery/key")
    String publicKey();

    @PostMapping(path = "/sign")
    AccessToken sign(Map<String, Object> data);

    @Component
    class Fallback implements AuthorizationManagerClient {
        @Override
        public String publicKey() {
            return "";
        }

        @Override
        public AccessToken sign(Map<String, Object> data) {
            return AccessToken.builder().build();
        }
    }
}
