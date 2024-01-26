package com.test.task.auth.controller;


import com.test.task.auth.model.AccessToken;
import com.test.task.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthRestController {
    private final AuthService authService;

    @GetMapping("/discovery/key")
    public ResponseEntity<String> publicKey() {
        return ResponseEntity.ok()
                .body(Base64.encodeBase64String(authService.getPublicKey().getEncoded()));
    }

    @PostMapping("/sign")
    public ResponseEntity<AccessToken> sign(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok()
                .body(authService.sign(data));
    }
}
