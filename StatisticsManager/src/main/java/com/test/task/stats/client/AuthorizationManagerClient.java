package com.test.task.stats.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "authorization.manager", path = "/auth", fallback = AuthorizationManagerClient.Fallback.class)
public interface AuthorizationManagerClient {
    @GetMapping(path = "/discovery/key")
    String publicKey();

    @Component
    class Fallback implements AuthorizationManagerClient {
        @Override
        public String publicKey() {
            return "";
        }
    }
}
