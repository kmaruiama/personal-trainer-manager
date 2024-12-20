package com.example.training_manager.Security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SecurityConstant {
    public static final long jwtExpirationDate = 86400000;
    public static final PrivateKey jwtPrivateKey;
    public static final PublicKey jwtPublicKey;

    static {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            keyGen.initialize(256);
            KeyPair keyPair = keyGen.generateKeyPair();
            jwtPrivateKey = keyPair.getPrivate();
            jwtPublicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Falha ao gerar um par ec", e);
        }
    }
}
