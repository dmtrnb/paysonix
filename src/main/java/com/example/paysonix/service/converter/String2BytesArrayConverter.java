package com.example.paysonix.service.converter;

import org.springframework.core.convert.converter.Converter;

public interface String2BytesArrayConverter extends Converter<String, byte[]> {

    @Override
    byte[] convert(String source);
}

