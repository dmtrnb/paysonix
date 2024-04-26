package com.example.paysonix.service.converter;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HexFormat;

@Primary
@Component
public class BytesArray2HexStringConverter implements BytesArray2StringConverter {

    @Override
    public String convert(byte[] source) {
        return HexFormat.of().formatHex(source);
    }
}
