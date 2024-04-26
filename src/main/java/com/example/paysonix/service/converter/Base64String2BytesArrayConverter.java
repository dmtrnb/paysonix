package com.example.paysonix.service.converter;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class Base64String2BytesArrayConverter implements String2BytesArrayConverter {

    @Override
    public byte[] convert(String source) {
        return Base64.getDecoder().decode(source);
    }
}
