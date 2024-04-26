package com.example.paysonix.service;

import com.example.paysonix.service.converter.BytesArray2StringConverter;
import com.example.paysonix.service.converter.String2BytesArrayConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

@Component
public class SecretCipher {

    private static final int BYTES_NUMBER = 16;

    private final Cipher encoder;
    private final Cipher decoder;
    private final BytesArray2StringConverter bytesArray2StringConverter;
    private final String2BytesArrayConverter string2BytesArrayConverter;

    public SecretCipher(@Value("${cipher.algorithm}") String algorithm,
                        @Qualifier("bytesArrayToBase64StringConverter") BytesArray2StringConverter bytesArray2StringConverter,
                        @Autowired String2BytesArrayConverter string2BytesArrayConverter) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[BYTES_NUMBER];
        secureRandom.nextBytes(randomBytes);
        SecretKeySpec secretKeySpec = new SecretKeySpec(randomBytes, algorithm);

        encoder = Cipher.getInstance(algorithm);
        encoder.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        decoder = Cipher.getInstance(algorithm);
        decoder.init(Cipher.DECRYPT_MODE, secretKeySpec);

        this.bytesArray2StringConverter = bytesArray2StringConverter;
        this.string2BytesArrayConverter = string2BytesArrayConverter;
    }

    public String encrypt(String plainText) throws IllegalBlockSizeException, BadPaddingException {
        byte[] encrypted = encoder.doFinal(plainText.getBytes());
        return bytesArray2StringConverter.convert(encrypted);
    }

    public String decrypt(String encryptedText) throws IllegalBlockSizeException, BadPaddingException {
        byte[] encrypted = Objects.requireNonNull(string2BytesArrayConverter.convert(encryptedText));
        byte[] decrypted = decoder.doFinal(encrypted);
        return new String(decrypted);
    }
}
