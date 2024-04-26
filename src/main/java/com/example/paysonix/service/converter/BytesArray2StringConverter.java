package com.example.paysonix.service.converter;

import org.springframework.core.convert.converter.Converter;

public interface BytesArray2StringConverter extends Converter<byte[], String> {

    @Override
    String convert(byte[] source);
}
