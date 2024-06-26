package com.example.paysonix.service;

import com.example.paysonix.domain.response.Result;
import com.example.paysonix.service.converter.BytesArray2StringConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SomeService {

    public static final String KEY_VALUE_SEPARATOR = "=";
    public static final String KEY_VALUE_PAIR_SEPARATOR = "&";

    private final Mac mac;
    private final BytesArray2StringConverter converter;

    public List<Result> doSomething(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return List.of();
        }

        String sortedParamsString = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + KEY_VALUE_SEPARATOR + entry.getValue())
                .collect(Collectors.joining(KEY_VALUE_PAIR_SEPARATOR));

        byte[] hmac = mac.doFinal(sortedParamsString.getBytes());

        String resultString = converter.convert(hmac);

        return List.of(new Result(resultString));
    }
}
