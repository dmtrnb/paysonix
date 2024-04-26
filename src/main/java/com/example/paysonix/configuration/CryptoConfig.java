package com.example.paysonix.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class CryptoConfig {

    @Bean
    public Mac mac(@Value("${encryption.key}") String key,
                   @Value("${encryption.algorithm}") String algorithm) throws InvalidKeyException, NoSuchAlgorithmException {
        Mac mac = Mac.getInstance(algorithm);

        mac.init(new SecretKeySpec(key.getBytes(), algorithm));

        return mac;
    }
}
