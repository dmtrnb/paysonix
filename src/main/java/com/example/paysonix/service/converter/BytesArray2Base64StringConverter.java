package com.example.paysonix.service.converter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Qualifier("bytesArrayToBase64StringConverter")
public class BytesArray2Base64StringConverter implements BytesArray2StringConverter {

    @Override
    public String convert(byte[] source) {
        return Base64.getEncoder().encodeToString(source);
    }
}
