package com.test.task.auth.service.vault;

import io.jsonwebtoken.Jwts;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

@Getter
@Service
public class VaultServiceImpl implements VaultService {
    private static final String ALGORITHM = "RSA";
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public VaultServiceImpl() {
        KeyPair keyPair = generateKeyPair();
        this.privateKey = generatePrivateKey(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));
        this.publicKey = generatePublicKey(Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
    }

    @SneakyThrows
    private KeyPair generateKeyPair() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    @SneakyThrows
    private PrivateKey generatePrivateKey(String jwtPrivateKey) {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] keyBytes = Base64.decodeBase64(jwtPrivateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    @SneakyThrows
    private PublicKey generatePublicKey(String jwtPublicKey) {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] keyBytes = Base64.decodeBase64(jwtPublicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    @Override
    public String sign(Map<String, Object> headerClaims, Map<String, Object> payloadClaims) {
        return Jwts.builder()
                .header().add(headerClaims).and()
                .claims(payloadClaims)
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }
}
