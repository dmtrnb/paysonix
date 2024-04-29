package com.example.paysonix.service;


import com.example.paysonix.service.converter.Base64String2BytesArrayConverter;
import com.example.paysonix.service.converter.BytesArray2Base64StringConverter;
import com.example.paysonix.service.converter.BytesArray2StringConverter;
import com.example.paysonix.service.converter.String2BytesArrayConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SecretCipherTest {

    @Autowired
    private SecretCipher secretCipher;

    private static final String TEST_STRING = "hello";

    @Test
    public void testThatDecodeEncryptedStringReturnRight() throws IllegalBlockSizeException, BadPaddingException {
        String encrypted = secretCipher.encrypt(TEST_STRING);
        String decrypted = secretCipher.decrypt(encrypted);

        assertEquals(TEST_STRING, decrypted);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public String2BytesArrayConverter string2BytesArrayConverter() {
            return new Base64String2BytesArrayConverter();
        }

        @Bean
        public BytesArray2StringConverter bytesArray2StringConverter() {
            return new BytesArray2Base64StringConverter();
        }
    }
}
